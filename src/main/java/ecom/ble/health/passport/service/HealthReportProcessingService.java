package ecom.ble.health.passport.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.ble.health.passport.analyzer.HealthReportAnalyzerService;
import ecom.ble.health.passport.entity.MedicalRecordShortEntity;
import ecom.ble.health.passport.entity.UserFileEntity;
import ecom.ble.health.passport.exception.FileStorageException;
import ecom.ble.health.passport.model.Diagnosis;
import ecom.ble.health.passport.model.MedicalRecordShort;
import ecom.ble.health.passport.model.record.MedicalRecord;
import ecom.ble.health.passport.repository.MedicalRecordShortRepository;
import ecom.ble.health.passport.repository.UserFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthReportProcessingService {

    private final HealthReportAnalyzerService healthReportAnalyzerService;
    private final UserFileRepository userFileRepository;
    private final MedicalRecordShortRepository medicalRecordShortRepository;
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path analysisStorageLocation;

    @PostConstruct
    public void init() {
        this.analysisStorageLocation = Paths.get(uploadDir, "analysis").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.analysisStorageLocation);
            log.info("Initialized analysis storage location: {}", this.analysisStorageLocation);
        } catch (IOException ex) {
            log.error("Failed to create analysis directory: {}", this.analysisStorageLocation, ex);
            throw new FileStorageException("Could not create the directory for analysis results: " + this.analysisStorageLocation, ex);
        }
    }

    @Async("healthReportExecutor")
    public void processHealthReport(String userId, Long fileId, String filename) {
        log.info("Starting async health report processing for userId: {}, fileId: {}, filename: {}", userId, fileId, filename);

        try {
            // Call the analyzer service
            JsonNode analysisResult = healthReportAnalyzerService.analyze(userId, filename, null);


            // Store the result to file
            // Ensure directory exists before writing
            if (analysisStorageLocation == null || !Files.exists(analysisStorageLocation)) {
                try {
                    this.analysisStorageLocation = Paths.get(uploadDir, "analysis").toAbsolutePath().normalize();
                    Files.createDirectories(this.analysisStorageLocation);
                    log.info("Created analysis directory: {}", this.analysisStorageLocation);
                } catch (IOException ex) {
                    throw new FileStorageException("Could not create the directory for analysis results: " + this.analysisStorageLocation, ex);
                }
            }
            
            String analysisFileName = userId + "_" + fileId + ".json";
            Path analysisFilePath = analysisStorageLocation.resolve(analysisFileName);
            
            // Ensure parent directory exists
            Path parentDir = analysisFilePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
                log.debug("Created parent directory: {}", parentDir);
            }
            
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(analysisFilePath.toFile(), analysisResult);

            log.info("Analysis result stored at: {}", analysisFilePath);
// Optional: map to strongly typed model (helps validate structure and enables typed usage elsewhere)
            try {
                MedicalRecord medicalRecord = mapAnalysisResultToMedicalRecord(analysisResult, userId, fileId);

                log.debug("Mapped analysis result to MedicalRecord for userId={}, fileId={}, recordId={}",
                        userId, fileId, medicalRecord.getRecordId());

                // Build MedicalRecordShort (API model) + persist a DB entity version
                MedicalRecordShort medicalRecordShort = new MedicalRecordShort();
                medicalRecordShort.setMedicalRecordId(String.valueOf(fileId));
                if (medicalRecord.getSummary() != null) {
                    medicalRecordShort.setCreatedAt(medicalRecord.getSummary().getReportDate());
                }

                Diagnosis diagnosis = new Diagnosis();
                if (medicalRecord.getSummary() != null) {
                    diagnosis.setDescription(medicalRecord.getSummary().getBrief());
                    diagnosis.setDiagnosisDate(medicalRecord.getSummary().getReportDate());
                }
                medicalRecordShort.setDiagnosis(diagnosis);

                medicalRecordShort.setSymptoms(safeFirstAreaOfConcernPotentialCause(medicalRecord));

                MedicalRecordShortEntity entity = toEntity(medicalRecordShort, userId);
                medicalRecordShortRepository.save(entity);
                log.info("Saved MedicalRecordShort: id={}, medicalRecordId={}, userId={}", entity.getId(), entity.getMedicalRecordId(), entity.getUserId());
            } catch (Exception mappingEx) {
                // Don't fail processing just because mapping failed; still persist the raw analysis JSON
                log.warn("Failed to map analysis result to MedicalRecord for userId={}, fileId={}. Storing raw JSON. error={}",
                        userId, fileId, mappingEx.getMessage());
            }
            // Update the entity to mark as processed
            userFileRepository.findById(fileId).ifPresent(entity -> {
                entity.setProcessed(true);
                entity.setProcessedAt(Instant.now());
                userFileRepository.save(entity);
                log.info("Marked file as processed: fileId={}", fileId);
            });

        } catch (Exception ex) {
            log.error("Failed to process health report for userId: {}, fileId: {}, filename: {}", userId, fileId, filename, ex);
        }
    }

    private String safeFirstAreaOfConcernPotentialCause(MedicalRecord medicalRecord) {
        if (medicalRecord == null || medicalRecord.getAreasOfConcern() == null || medicalRecord.getAreasOfConcern().isEmpty()) {
            return null;
        }
        var firstConcern = medicalRecord.getAreasOfConcern().get(0);
        if (firstConcern == null || firstConcern.getPotentialCauses() == null || firstConcern.getPotentialCauses().isEmpty()) {
            return null;
        }
        return firstConcern.getPotentialCauses().get(0);
    }

    private MedicalRecordShortEntity toEntity(MedicalRecordShort medicalRecordShort, String userId) {
        MedicalRecordShortEntity entity = new MedicalRecordShortEntity();
        entity.setUserId(userId);
        entity.setMedicalRecordId(medicalRecordShort.getMedicalRecordId());
        entity.setEncounterDate(medicalRecordShort.getEncounterDate());
        entity.setRecordType(medicalRecordShort.getRecordType());
        entity.setSymptoms(medicalRecordShort.getSymptoms());
        entity.setCreatedAt(medicalRecordShort.getCreatedAt());
        entity.setUpdatedAt(medicalRecordShort.getUpdatedAt());
        entity.setStatus(medicalRecordShort.getStatus());

        Diagnosis d = medicalRecordShort.getDiagnosis();
        if (d != null) {
            entity.setDiagnosisId(d.getId());
            entity.setDiagnosisDate(d.getDiagnosisDate());
            entity.setDiagnosisName(d.getDiagnosisName());
            entity.setDiagnosisType(d.getDiagnosisType());
            entity.setDiagnosisSeverity(d.getSeverity());
            entity.setDiagnosisDescription(d.getDescription());
        }

        if (medicalRecordShort.getMedications() != null) {
            try {
                entity.setMedications(objectMapper.writeValueAsString(medicalRecordShort.getMedications()));
            } catch (Exception ex) {
                throw new FileStorageException("Failed to serialize medications as JSON: " + ex.getMessage(), ex);
            }
        }

        return entity;
    }

    public MedicalRecord mapAnalysisResultToMedicalRecord(JsonNode analysisResult, String userId, Long fileId) {
        if (analysisResult == null || analysisResult.isNull()) {
            throw new FileStorageException("Analysis result is empty (cannot map to MedicalRecord).");
        }

        try {
            MedicalRecord medicalRecord = objectMapper.treeToValue(analysisResult, MedicalRecord.class);
            // Ensure recordId is always present even if the AI didn't provide it
            if (medicalRecord != null && (medicalRecord.getRecordId() == null || medicalRecord.getRecordId().isBlank())) {
                medicalRecord.setRecordId(userId + "_" + fileId);
            }
            return medicalRecord;
        } catch (Exception ex) {
            throw new FileStorageException("Failed to map analysis JSON to MedicalRecord: " + ex.getMessage(), ex);
        }
    }

    public MedicalRecord readAnalysisResultAsMedicalRecord(String userId, Long fileId) {
        String analysisJson = readAnalysisResultAsJson(userId, fileId);
        try {
            MedicalRecord medicalRecord = objectMapper.readValue(analysisJson, MedicalRecord.class);
            if (medicalRecord != null && (medicalRecord.getRecordId() == null || medicalRecord.getRecordId().isBlank())) {
                medicalRecord.setRecordId(userId + "_" + fileId);
            }
            return medicalRecord;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to parse analysis result into MedicalRecord: " + ex.getMessage(), ex);
        }
    }

    public String readAnalysisResultAsJson(String userId, Long fileId) {
        // Validate that the file belongs to the user
        UserFileEntity entity = userFileRepository.findByUserIdAndFileId(userId, fileId)
                .orElseThrow(() -> new FileStorageException("File not found for userId: " + userId + ", fileId: " + fileId));

        if (!entity.isProcessed()) {
            throw new FileStorageException("Analysis not yet complete for fileId: " + fileId);
        }

        String analysisFileName = userId + "_" + fileId + ".json";
        Path analysisFilePath = analysisStorageLocation.resolve(analysisFileName);

        if (!Files.exists(analysisFilePath)) {
            throw new FileStorageException("Analysis file not found: " + analysisFileName);
        }

        try {
            return Files.readString(analysisFilePath);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to read analysis result: " + ex.getMessage(), ex);
        }
    }
}

