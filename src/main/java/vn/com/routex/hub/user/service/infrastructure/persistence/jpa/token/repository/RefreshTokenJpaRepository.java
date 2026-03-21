package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.user.service.domain.token.model.RefreshTokenStatus;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.token.entity.RefreshTokenJpaEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, String> {

    Optional<RefreshTokenJpaEntity> findByToken(String token);

    @Modifying
    @Query("""
            UPDATE RefreshTokenJpaEntity t
            SET t.status = :newStatus,
                t.revokedAt = :revokeAt,
                t.updatedAt = :revokeAt
            WHERE t.userId = :userId
            AND t.status = :currentStatus
            """)
    void updateAllByUserIdAndStatus(@Param("userId") String userId,
                                    @Param("currentStatus") RefreshTokenStatus currentStatus,
                                    @Param("newStatus") RefreshTokenStatus newStatus,
                                    @Param("revokeAt") OffsetDateTime revokeAt);
}
