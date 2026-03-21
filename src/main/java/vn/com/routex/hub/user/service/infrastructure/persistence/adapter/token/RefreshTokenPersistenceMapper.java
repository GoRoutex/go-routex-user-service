package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.token;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.token.model.RefreshToken;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.token.entity.RefreshTokenJpaEntity;

@Component
public class RefreshTokenPersistenceMapper {

    public RefreshTokenJpaEntity toJpaEntity(RefreshToken refreshToken) {
        return RefreshTokenJpaEntity.builder()
                .id(refreshToken.getId())
                .userId(refreshToken.getUserId())
                .token(refreshToken.getToken())
                .status(refreshToken.getStatus())
                .issuedAt(refreshToken.getIssuedAt())
                .expiredAt(refreshToken.getExpiredAt())
                .usedAt(refreshToken.getUsedAt())
                .revokedAt(refreshToken.getRevokedAt())
                .createdAt(refreshToken.getCreatedAt())
                .createdBy(refreshToken.getCreatedBy())
                .updatedAt(refreshToken.getUpdatedAt())
                .updatedBy(refreshToken.getUpdatedBy())
                .build();
    }

    public RefreshToken toDomain(RefreshTokenJpaEntity refreshTokenJpaEntity) {
        return RefreshToken.builder()
                .id(refreshTokenJpaEntity.getId())
                .userId(refreshTokenJpaEntity.getUserId())
                .token(refreshTokenJpaEntity.getToken())
                .status(refreshTokenJpaEntity.getStatus())
                .issuedAt(refreshTokenJpaEntity.getIssuedAt())
                .expiredAt(refreshTokenJpaEntity.getExpiredAt())
                .usedAt(refreshTokenJpaEntity.getUsedAt())
                .revokedAt(refreshTokenJpaEntity.getRevokedAt())
                .createdAt(refreshTokenJpaEntity.getCreatedAt())
                .createdBy(refreshTokenJpaEntity.getCreatedBy())
                .updatedAt(refreshTokenJpaEntity.getUpdatedAt())
                .updatedBy(refreshTokenJpaEntity.getUpdatedBy())
                .build();
    }
}
