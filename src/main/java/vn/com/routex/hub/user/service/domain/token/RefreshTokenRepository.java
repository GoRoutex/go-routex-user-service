package vn.com.routex.hub.user.service.domain.token;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndStatus(String token, RefreshTokenStatus status);
    @Modifying
    @Query("""
            UPDATE RefreshToken T
            SET T.status = :newStatus,
                T.revokedAt = :revokedAt,
                T.updatedAt = :revokedAt
            WHERE T.userId = :userId
            AND T.status = :currentStatus
            """)
    void updateAllByUserIdAndStatus(@Param("userId") String userId,
                                   @Param("currentStatus") RefreshTokenStatus currentStatus,
                                   @Param("newStatus") RefreshTokenStatus newStatus,
                                   @Param("revokeAt") OffsetDateTime revokeAt);
}
