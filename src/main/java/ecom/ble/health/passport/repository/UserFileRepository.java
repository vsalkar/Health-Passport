package ecom.ble.health.passport.repository;

import ecom.ble.health.passport.entity.UserFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFileEntity, Long> {

    Optional<UserFileEntity> findFirstByUserIdAndStoredFileName(String userId, String storedFileName);

    Optional<UserFileEntity> findFirstByUserIdAndStoredFileNameAndIsDeletedFalse(String userId, String storedFileName);

    List<UserFileEntity> findByUserIdAndIsDeletedFalseOrderByUploadedAtDesc(String userId);

    Optional<UserFileEntity> findByUserIdAndFileId(String userId, Long fileId);
}


