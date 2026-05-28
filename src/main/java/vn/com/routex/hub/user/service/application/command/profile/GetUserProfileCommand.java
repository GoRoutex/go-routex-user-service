package vn.com.routex.hub.user.service.application.command.profile;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;

@Builder
public record GetUserProfileCommand(
        RequestContext context,
        String userId
) {
}

