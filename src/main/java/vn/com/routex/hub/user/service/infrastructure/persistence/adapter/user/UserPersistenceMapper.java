package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.user;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.user.entity.UserJpaEntity;

@Component
public class UserPersistenceMapper {

    public UserJpaEntity toJpaEntity(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .passwordHash(user.getPasswordHash())
                .dob(user.getDob())
                .phoneNumber(user.getPhoneNumber())
                .phoneVerified(user.getPhoneVerified())
                .email(user.getEmail())
                .emailVerified(user.getEmailVerified())
                .status(user.getStatus())
                .tenantId(user.getTenantId())
                .language(user.getLanguage())
                .timezone(user.getTimezone())
                .failLoginCount(user.getFailLoginCount())
                .lastLoginAt(user.getLastLoginAt())
                .lockedUntil(user.getLockedUntil())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

    public User toDomain(UserJpaEntity userJpaEntity) {
        return User.builder()
                .id(userJpaEntity.getId())
                .username(userJpaEntity.getUsername())
                .fullName(userJpaEntity.getFullName())
                .passwordHash(userJpaEntity.getPasswordHash())
                .dob(userJpaEntity.getDob())
                .phoneNumber(userJpaEntity.getPhoneNumber())
                .phoneVerified(userJpaEntity.getPhoneVerified())
                .email(userJpaEntity.getEmail())
                .emailVerified(userJpaEntity.getEmailVerified())
                .status(userJpaEntity.getStatus())
                .tenantId(userJpaEntity.getTenantId())
                .language(userJpaEntity.getLanguage())
                .timezone(userJpaEntity.getTimezone())
                .failLoginCount(userJpaEntity.getFailLoginCount())
                .lastLoginAt(userJpaEntity.getLastLoginAt())
                .lockedUntil(userJpaEntity.getLockedUntil())
                .createdAt(userJpaEntity.getCreatedAt())
                .createdBy(userJpaEntity.getCreatedBy())
                .updatedAt(userJpaEntity.getUpdatedAt())
                .updatedBy(userJpaEntity.getUpdatedBy())
                .build();
    }
}
