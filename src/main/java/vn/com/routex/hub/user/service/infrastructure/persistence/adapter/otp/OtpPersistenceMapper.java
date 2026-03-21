package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.otp;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.otp.model.Otp;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.otp.entity.OtpJpaEntity;

@Component
public class OtpPersistenceMapper {

    public OtpJpaEntity toJpaEntity(Otp otp) {
        return OtpJpaEntity.builder()
                .id(otp.getId())
                .userId(otp.getUserId())
                .phoneNumber(otp.getPhoneNumber())
                .email(otp.getEmail())
                .fullName(otp.getFullName())
                .purpose(otp.getPurpose())
                .status(otp.getStatus())
                .otpHash(otp.getOtpHash())
                .producedAt(otp.getProducedAt())
                .attemptCount(otp.getAttemptCount())
                .expiredAt(otp.getExpiredAt())
                .consumedAt(otp.getConsumedAt())
                .createdAt(otp.getCreatedAt())
                .createdBy(otp.getCreatedBy())
                .updatedAt(otp.getUpdatedAt())
                .updatedBy(otp.getUpdatedBy())
                .build();
    }

    public Otp toDomain(OtpJpaEntity otpJpaEntity) {
        return Otp.builder()
                .id(otpJpaEntity.getId())
                .userId(otpJpaEntity.getUserId())
                .phoneNumber(otpJpaEntity.getPhoneNumber())
                .email(otpJpaEntity.getEmail())
                .fullName(otpJpaEntity.getFullName())
                .purpose(otpJpaEntity.getPurpose())
                .status(otpJpaEntity.getStatus())
                .otpHash(otpJpaEntity.getOtpHash())
                .producedAt(otpJpaEntity.getProducedAt())
                .attemptCount(otpJpaEntity.getAttemptCount())
                .expiredAt(otpJpaEntity.getExpiredAt())
                .consumedAt(otpJpaEntity.getConsumedAt())
                .createdAt(otpJpaEntity.getCreatedAt())
                .createdBy(otpJpaEntity.getCreatedBy())
                .updatedAt(otpJpaEntity.getUpdatedAt())
                .updatedBy(otpJpaEntity.getUpdatedBy())
                .build();
    }
}
