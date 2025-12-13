package ecom.ble.health.passport.controller.impl;

import ecom.ble.health.passport.controller.HealthPassportController;
import ecom.ble.health.passport.model.Diagnosis;
import ecom.ble.health.passport.model.MedicalRecord;
import ecom.ble.health.passport.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class DefaultHealthPassportController implements HealthPassportController {
    @Override
    public ResponseEntity<User> getUserHealthDetails(String id) {
        var user = new User();
        var records = new ArrayList<MedicalRecord>();
        MedicalRecord medicalRecord = new MedicalRecord();
        Diagnosis diagnosis = new Diagnosis();
        user.setId("122345");
        user.setName("John Smith");
        user.setAge("30");
        user.setBloodGroup("B+");
        user.setContactNumber("1234567890");
        records.add(medicalRecord);
        medicalRecord.setRecordType("Consultation");
        medicalRecord.setMedicalRecordId("med123");
        medicalRecord.setStatus("Completed");
        medicalRecord.setSymptoms("Fever, Cold");
        medicalRecord.setCreatedAt("12/12/2025");
        medicalRecord.setUpdatedAt("12/12/2025");
        diagnosis.setId("dia1233");
        diagnosis.setDiagnosisDate("12/12/2025");
        diagnosis.setDiagnosisName("Temperature Analysis");
        diagnosis.setDiagnosisType("Lab Test");
        diagnosis.setSeverity("High");
        diagnosis.setDescription("High Temp and cold analysis");
        medicalRecord.setDiagnosis(diagnosis);
        user.setMedicalRecords(records);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
