package ecom.ble.health.passport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medical_record_short")
public class MedicalRecordShortEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "medical_record_id", length = 255)
    private String medicalRecordId;

    @Column(name = "encounter_date", length = 50)
    private String encounterDate;

    @Column(name = "record_type", length = 100)
    private String recordType;

    // Flattened diagnosis fields (Diagnosis is a model object, not a JPA entity)
    @Column(name = "diagnosis_id", length = 255)
    private String diagnosisId;

    @Column(name = "diagnosis_date", length = 50)
    private String diagnosisDate;

    @Column(name = "diagnosis_name", length = 255)
    private String diagnosisName;

    @Column(name = "diagnosis_type", length = 100)
    private String diagnosisType;

    @Column(name = "diagnosis_severity", length = 50)
    private String diagnosisSeverity;

    @Column(name = "diagnosis_description", columnDefinition = "text")
    private String diagnosisDescription;

    @Column(name = "symptoms", columnDefinition = "text")
    private String symptoms;

    @Column(name = "created_at", length = 50)
    private String createdAt;

    @Column(name = "updated_at", length = 50)
    private String updatedAt;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "health_score")
    private Integer healthScore;

    // Stored as JSON string in MySQL JSON column (or TEXT if your table differs)
    @Column(name = "medications", columnDefinition = "json")
    private String medications;
}


