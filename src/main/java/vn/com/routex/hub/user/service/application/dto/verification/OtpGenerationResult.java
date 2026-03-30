package vn.com.routex.hub.user.service.application.dto.verification;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record OtpGenerationResult(
        String plainOtp,
        String fullName,
        String email,
        String userId,
        OffsetDateTime expiredAt,
        Long expiresMinutes
) {
}

