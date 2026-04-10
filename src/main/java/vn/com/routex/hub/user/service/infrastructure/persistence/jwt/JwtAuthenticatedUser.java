package vn.com.routex.hub.user.service.infrastructure.persistence.jwt;

import org.springframework.security.core.AuthenticatedPrincipal;

import java.util.Set;

public record JwtAuthenticatedUser(
        String userId,
        String email,
        String merchantId,
        Set<String> roles,
        Set<String> authorities
) implements AuthenticatedPrincipal {

    @Override
    public String getName() {
        return email != null && !email.isBlank() ? email : userId;
    }
}
