package ecom.ble.health.passport.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter

public class User {
    private String name;
    private String id;
    private String age;
    private String bloodGroup;
    private String contactNumber;
    private List<MedicalRecordShort> ongoingDiagnosis;
    private List<MedicalRecordShort> medicalRecordShorts;
    private List<String> alerts;
    private List<String> suggestions;
}
