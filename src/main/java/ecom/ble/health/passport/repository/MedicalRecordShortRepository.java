package ecom.ble.health.passport.repository;

import ecom.ble.health.passport.entity.MedicalRecordShortEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordShortRepository extends JpaRepository<MedicalRecordShortEntity, Long> {
    List<MedicalRecordShortEntity> findAllMedicalRecordShortEntitiesByUserId(String userId);
}


