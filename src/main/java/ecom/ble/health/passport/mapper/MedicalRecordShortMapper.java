package ecom.ble.health.passport.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.ble.health.passport.entity.MedicalRecordShortEntity;
import ecom.ble.health.passport.exception.FileStorageException;
import ecom.ble.health.passport.model.Diagnosis;
import ecom.ble.health.passport.model.MedicalRecordShort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicalRecordShortMapper {

    private final ObjectMapper objectMapper;

    /**
     * Maps MedicalRecordShortEntity to MedicalRecordShort model
     * @param entity the entity to map
     * @return the mapped model
     */
    public MedicalRecordShort toModel(MedicalRecordShortEntity entity) {
        if (entity == null) {
            return null;
        }

        MedicalRecordShort model = new MedicalRecordShort();
        model.setMedicalRecordId(entity.getMedicalRecordId());
        model.setEncounterDate(entity.getEncounterDate());
        model.setRecordType("Lab Report");
        model.setSymptoms(entity.getSymptoms());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setStatus(entity.getStatus());
        model.setHealthScore(entity.getHealthScore());

        // Map flattened diagnosis fields to Diagnosis object
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(entity.getDiagnosisId());
        diagnosis.setDiagnosisDate(entity.getDiagnosisDate());
        diagnosis.setDiagnosisName(entity.getDiagnosisName());
        diagnosis.setDiagnosisType(entity.getDiagnosisType());
        diagnosis.setSeverity(entity.getDiagnosisSeverity() != null ? entity.getDiagnosisSeverity() : "low");
        diagnosis.setDescription(entity.getDiagnosisDescription());

        // Only set diagnosis if at least one field is not null/empty
        if (hasDiagnosisData(diagnosis)) {
            model.setDiagnosis(diagnosis);
        }

        // Deserialize medications from JSON string to List<String>
        if (entity.getMedications() != null && !entity.getMedications().trim().isEmpty()) {
            try {
                List<String> medications = objectMapper.readValue(
                        entity.getMedications(),
                        new TypeReference<List<String>>() {}
                );
                model.setMedications(medications);
            } catch (Exception ex) {
                throw new FileStorageException("Failed to deserialize medications from JSON: " + ex.getMessage(), ex);
            }
        } else {
            model.setMedications(new ArrayList<>());
        }

        return model;
    }

    /**
     * Maps MedicalRecordShort model to MedicalRecordShortEntity
     * @param model the model to map
     * @param userId the user ID to associate with the entity
     * @return the mapped entity
     */
    public MedicalRecordShortEntity toEntity(MedicalRecordShort model, String userId) {
        if (model == null) {
            return null;
        }

        MedicalRecordShortEntity entity = new MedicalRecordShortEntity();
        entity.setUserId(userId);
        entity.setMedicalRecordId(model.getMedicalRecordId());
        entity.setEncounterDate(model.getEncounterDate());
        entity.setRecordType(model.getRecordType());
        entity.setSymptoms(model.getSymptoms());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setStatus(model.getStatus());
        entity.setHealthScore(model.getHealthScore());

        // Map Diagnosis object to flattened fields
        Diagnosis diagnosis = model.getDiagnosis();
        if (diagnosis != null) {
            entity.setDiagnosisId(diagnosis.getId());
            entity.setDiagnosisDate(diagnosis.getDiagnosisDate());
            entity.setDiagnosisName(diagnosis.getDiagnosisName());
            entity.setDiagnosisType(diagnosis.getDiagnosisType());
            entity.setDiagnosisSeverity(diagnosis.getSeverity());
            entity.setDiagnosisDescription(diagnosis.getDescription());
        }

        // Serialize medications from List<String> to JSON string
        if (model.getMedications() != null && !model.getMedications().isEmpty()) {
            try {
                entity.setMedications(objectMapper.writeValueAsString(model.getMedications()));
            } catch (Exception ex) {
                throw new FileStorageException("Failed to serialize medications as JSON: " + ex.getMessage(), ex);
            }
        }

        return entity;
    }

    /**
     * Helper method to check if diagnosis has any meaningful data
     */
    private boolean hasDiagnosisData(Diagnosis diagnosis) {
        return diagnosis != null && (
                (diagnosis.getId() != null && !diagnosis.getId().trim().isEmpty()) ||
                (diagnosis.getDiagnosisDate() != null && !diagnosis.getDiagnosisDate().trim().isEmpty()) ||
                (diagnosis.getDiagnosisName() != null && !diagnosis.getDiagnosisName().trim().isEmpty()) ||
                (diagnosis.getDiagnosisType() != null && !diagnosis.getDiagnosisType().trim().isEmpty()) ||
                (diagnosis.getSeverity() != null && !diagnosis.getSeverity().trim().isEmpty()) ||
                (diagnosis.getDescription() != null && !diagnosis.getDescription().trim().isEmpty())
        );
    }
}

