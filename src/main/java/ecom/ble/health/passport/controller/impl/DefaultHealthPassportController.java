package ecom.ble.health.passport.controller.impl;

import ecom.ble.health.passport.controller.HealthPassportController;
import ecom.ble.health.passport.entity.MedicalRecordShortEntity;
import ecom.ble.health.passport.entity.UserEntity;
import ecom.ble.health.passport.mapper.MedicalRecordShortMapper;
import ecom.ble.health.passport.model.Diagnosis;
import ecom.ble.health.passport.model.MedicalRecordShort;
import ecom.ble.health.passport.model.User;
import ecom.ble.health.passport.repository.MedicalRecordShortRepository;
import ecom.ble.health.passport.repository.UserRepository;
import ecom.ble.health.passport.service.HealthReportProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DefaultHealthPassportController implements HealthPassportController {

    private final HealthReportProcessingService healthReportProcessingService;
    private final MedicalRecordShortRepository medicalRecordShortRepository;
    private final UserRepository userRepository;
    private final MedicalRecordShortMapper medicalRecordShortMapper;
    @Override
    public ResponseEntity<User> getUserHealthDetails(String userId) {
        List<MedicalRecordShortEntity> medicalRecordShortEntities = medicalRecordShortRepository.findAllMedicalRecordShortEntitiesByUserId(userId);
        UserEntity userEntity = userRepository.findById(userId).get();
        var user = new User();
        var records = new ArrayList<MedicalRecordShort>();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setAge(userEntity.getAge().toString());
        user.setBloodGroup(userEntity.getBloodGroup());
        user.setContactNumber(userEntity.getContactNumber());
        medicalRecordShortEntities.forEach(record -> {
            records.add(medicalRecordShortMapper.toModel(record));
        });
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
