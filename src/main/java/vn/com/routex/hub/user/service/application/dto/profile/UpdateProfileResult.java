package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.Builder;

@Builder
public record UpdateProfileResult(
        String userId,
        String fullName,
        String email,
        String address,
        String phoneNumber,
        String avatarUrl
) {
}
