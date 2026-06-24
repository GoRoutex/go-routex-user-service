package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.user.entity.UserEntity;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    @Query("""
            SELECT u FROM UserEntity u
            WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<UserEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, String excludedId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, String excludedId);
}
