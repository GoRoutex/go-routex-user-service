package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Builder
public record CompleteProfileCommand(
        RequestContext context,
        String userId,
        String fullName,
        String nationalId,
        String avatarUrl,
        String address,
        String gender
) {
}

