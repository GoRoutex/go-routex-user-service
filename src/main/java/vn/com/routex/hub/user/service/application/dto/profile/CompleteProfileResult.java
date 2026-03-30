package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.Builder;

@Builder
public record CompleteProfileResult(
        String userId,
        String fullName,
        String gender,
        String avatarUrl,
        String address,
        Boolean profileCompleted
) {
}

