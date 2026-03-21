package vn.com.routex.hub.user.service.domain.token.port;

import vn.com.routex.hub.user.service.domain.token.model.RefreshToken;
import vn.com.routex.hub.user.service.domain.token.model.RefreshTokenStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface RefreshTokenRepositoryPort {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    void updateAllByUserIdAndStatus(String userId,
                                    RefreshTokenStatus currentStatus,
                                    RefreshTokenStatus newStatus,
                                    OffsetDateTime revokeAt);
}
