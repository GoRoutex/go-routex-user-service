package vn.com.routex.hub.user.service.application.command.verification;

import lombok.Builder;

@Builder
public record ResendVerificationResult(
        long expiresAfterSeconds,
        long retryAfterSeconds
) {
}

