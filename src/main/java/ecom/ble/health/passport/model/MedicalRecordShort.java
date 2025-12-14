package ecom.ble.health.passport.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class MedicalRecordShort {
    private String medicalRecordId;
    private String encounterDate;
    private String recordType;
    private Diagnosis diagnosis;
    private String symptoms;
    private String createdAt;
    private String updatedAt;
    private String status;
    private Integer healthScore;
    private List<String> medications;
}
