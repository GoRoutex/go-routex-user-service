package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.user.entity.UserEntity;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);


    boolean existsByPhoneNumber(String phoneNumber);

}
