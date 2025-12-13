package ecom.ble.health.passport.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Diagnosis {
    private String diagnosisName ;
    private String id;
    private String description;
    private String diagnosisType;
    private String diagnosisDate;
    private String severity;
}
