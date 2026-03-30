package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Builder
public record GetUserProfileCommand(
        RequestContext context,
        String userId
) {
}

