package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.Builder;

@Builder
public record GetMyProfileCommand(
        String userId
) {
}

