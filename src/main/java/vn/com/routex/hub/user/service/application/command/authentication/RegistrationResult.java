package vn.com.routex.hub.user.service.application.command.authentication;

import lombok.Builder;

@Builder
public record RegistrationResult(
        String userId,
        String email,
        String phoneNumber,
        String status
) {
}

