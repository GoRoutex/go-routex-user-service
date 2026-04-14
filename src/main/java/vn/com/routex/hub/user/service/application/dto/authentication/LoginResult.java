package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.Builder;

import java.util.Set;

@Builder
public record LoginResult(
        String accessToken,
        String refreshToken,
        String userId,
        String email,
        Set<String> roles,
        Set<String> authorities,
        Boolean profileCompleted,
        String avatarUrl
) {
}

