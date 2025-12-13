package ecom.ble.health.passport.controller.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ecom.ble.health.passport.controller.HealthPassportController;
import ecom.ble.health.passport.model.Diagnosis;
import ecom.ble.health.passport.model.MedicalRecord;
import ecom.ble.health.passport.model.User;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class DefaultHealthPassportController implements HealthPassportController {
    @Override
    public ResponseEntity<User> getUserHealthDetails(String userId) {
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
    @SneakyThrows
    @Override
    public ResponseEntity<HttpStatus> addMedicalRecord(ecom.ble.health.passport.model.record.MedicalRecord record, String userId) {

        var recordId = record.getRecordId();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        var filePath = userId.concat("_").concat(recordId).concat(".json");
       // ClassPathResource classPathResource = new ClassPathResource(filePath);
        gson.toJson(record,new FileWriter(filePath));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ecom.ble.health.passport.model.record.MedicalRecord> getRecordUserHealthDetails(String id) {
        return null;
    }

}
