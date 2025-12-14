package ecom.ble.health.passport.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.ble.health.passport.analyzer.HealthReportAnalyzerService;
import ecom.ble.health.passport.entity.UserFileEntity;
import ecom.ble.health.passport.exception.FileStorageException;
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
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path analysisStorageLocation;

    @PostConstruct
    public void init() {
        this.analysisStorageLocation = Paths.get(uploadDir, "analysis").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.analysisStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory for analysis results.", ex);
        }
    }

    @Async("healthReportExecutor")
    public void processHealthReport(String userId, Long fileId, String filename) {
        log.info("Starting async health report processing for userId: {}, fileId: {}, filename: {}", userId, fileId, filename);

        try {
            // Call the analyzer service
            JsonNode analysisResult = healthReportAnalyzerService.analyze(userId, filename, null);

            // Store the result to file
            String analysisFileName = userId + "_" + fileId + ".json";
            Path analysisFilePath = analysisStorageLocation.resolve(analysisFileName);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(analysisFilePath.toFile(), analysisResult);

            log.info("Analysis result stored at: {}", analysisFilePath);

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

