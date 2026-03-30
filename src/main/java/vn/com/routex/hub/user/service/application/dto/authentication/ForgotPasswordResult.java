package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.Builder;

@Builder
public record ForgotPasswordResult(
        String userId,
        Long expiresMinutes
) {
}

