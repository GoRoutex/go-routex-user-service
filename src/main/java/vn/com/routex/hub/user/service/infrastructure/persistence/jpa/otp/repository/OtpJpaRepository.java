package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.otp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.otp.entity.OtpJpaEntity;

import java.util.Optional;

public interface OtpJpaRepository extends JpaRepository<OtpJpaEntity, String> {

    Optional<OtpJpaEntity> findByUserId(String userId);

    @Query("""
    SELECT o FROM OtpJpaEntity o
    WHERE o.userId = :userId
      AND o.purpose = :purpose
      AND o.consumedAt IS NULL
    ORDER BY o.createdAt DESC
        """)
    Optional<OtpJpaEntity> findLatestActiveOtp(@Param("userId") String userId,
                                               @Param("purpose") OtpPurpose purpose);
}
