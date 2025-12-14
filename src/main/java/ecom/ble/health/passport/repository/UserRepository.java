package ecom.ble.health.passport.repository;

import ecom.ble.health.passport.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findById(String id);
}

