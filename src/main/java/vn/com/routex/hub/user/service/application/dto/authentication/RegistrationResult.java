package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.Builder;

@Builder
public record RegistrationResult(
        String userId,
        String email,
        String phoneNumber,
        String status
) {
}

