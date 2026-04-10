package vn.com.routex.hub.user.service.infrastructure.persistence.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.com.routex.hub.user.service.infrastructure.persistence.jwt.JwtAuthenticatedUser;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public final class SecurityContextUtils {

    private SecurityContextUtils() {
    }

    public static Optional<JwtAuthenticatedUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtAuthenticatedUser authenticatedUser) {
            return Optional.of(authenticatedUser);
        }

        return Optional.empty();
    }

    public static Optional<String> getCurrentMerchantId() {
        return getCurrentUser()
                .map(JwtAuthenticatedUser::merchantId)
                .filter(merchantId -> !merchantId.isBlank());
    }

    public static Set<String> getCurrentRoles() {
        return getCurrentUser()
                .map(JwtAuthenticatedUser::roles)
                .orElse(Collections.emptySet());
    }

    public static Set<String> getCurrentAuthorities() {
        return getCurrentUser()
                .map(JwtAuthenticatedUser::authorities)
                .orElse(Collections.emptySet());
    }
}
