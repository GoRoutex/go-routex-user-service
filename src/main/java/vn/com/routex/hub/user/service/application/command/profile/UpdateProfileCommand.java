package vn.com.routex.hub.user.service.application.command.profile;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;

@Builder
public record UpdateProfileCommand(
        RequestContext context,
        String userId,
        String fullName,
        String email,
        String phoneNumber,
        String address,
        String avatarUrl
) {
}
