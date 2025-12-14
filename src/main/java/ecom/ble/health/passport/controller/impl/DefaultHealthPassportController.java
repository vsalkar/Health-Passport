package ecom.ble.health.passport.controller.impl;

import ecom.ble.health.passport.controller.HealthPassportController;
import ecom.ble.health.passport.model.Diagnosis;
import ecom.ble.health.passport.model.MedicalRecordShort;
import ecom.ble.health.passport.model.User;
import ecom.ble.health.passport.service.HealthReportProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class DefaultHealthPassportController implements HealthPassportController {

    private final HealthReportProcessingService healthReportProcessingService;
    @Override
    public ResponseEntity<User> getUserHealthDetails(String userId) {
        var user = new User();
        var records = new ArrayList<MedicalRecordShort>();
        MedicalRecordShort medicalRecordShort = new MedicalRecordShort();
        Diagnosis diagnosis = new Diagnosis();
        user.setId("122345");
        user.setName("John Smith");
        user.setAge("30");
        user.setBloodGroup("B+");
        user.setContactNumber("1234567890");
        records.add(medicalRecordShort);
        medicalRecordShort.setRecordType("Consultation");
        medicalRecordShort.setMedicalRecordId("med123");
        medicalRecordShort.setStatus("Completed");
        medicalRecordShort.setSymptoms("Fever, Cold");
        medicalRecordShort.setCreatedAt("12/12/2025");
        medicalRecordShort.setUpdatedAt("12/12/2025");
        diagnosis.setId("dia1233");
        diagnosis.setDiagnosisDate("12/12/2025");
        diagnosis.setDiagnosisName("Temperature Analysis");
        diagnosis.setDiagnosisType("Lab Test");
        diagnosis.setSeverity("High");
        diagnosis.setDescription("High Temp and cold analysis");
        medicalRecordShort.setDiagnosis(diagnosis);
        user.setMedicalRecordShorts(records);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<String> getRecordUserHealthDetails(String fileid, String userId) {
        try {
            Long fileId = Long.parseLong(fileid);
            String analysisJson = healthReportProcessingService.readAnalysisResultAsJson(userId, fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(analysisJson);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
