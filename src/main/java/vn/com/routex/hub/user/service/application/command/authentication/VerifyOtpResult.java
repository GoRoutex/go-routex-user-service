package vn.com.routex.hub.user.service.application.command.authentication;

import lombok.Builder;

@Builder
public record VerifyOtpResult(
        String userId,
        String otpCode,
        String status
) {
}

