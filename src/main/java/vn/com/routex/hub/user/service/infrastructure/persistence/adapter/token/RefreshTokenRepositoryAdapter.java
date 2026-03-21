package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.token.model.RefreshToken;
import vn.com.routex.hub.user.service.domain.token.model.RefreshTokenStatus;
import vn.com.routex.hub.user.service.domain.token.port.RefreshTokenRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.token.repository.RefreshTokenJpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final RefreshTokenPersistenceMapper refreshTokenPersistenceMapper;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token).map(refreshTokenPersistenceMapper::toDomain);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenPersistenceMapper.toDomain(
                refreshTokenJpaRepository.save(refreshTokenPersistenceMapper.toJpaEntity(refreshToken))
        );
    }

    @Override
    public void updateAllByUserIdAndStatus(String userId,
                                           RefreshTokenStatus currentStatus,
                                           RefreshTokenStatus newStatus,
                                           OffsetDateTime revokeAt) {
        refreshTokenJpaRepository.updateAllByUserIdAndStatus(userId, currentStatus, newStatus, revokeAt);
    }
}
