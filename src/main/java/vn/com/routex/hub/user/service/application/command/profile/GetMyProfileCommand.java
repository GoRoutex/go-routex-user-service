package vn.com.routex.hub.user.service.application.command.profile;

import lombok.Builder;

@Builder
public record GetMyProfileCommand(
        String userId
) {
}

