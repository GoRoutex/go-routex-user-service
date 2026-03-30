package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Builder
public record ChangePasswordCommand(
        RequestContext context,
        String userId,
        String oldPassword,
        String newPassword,
        String confirmNewPassword
) {
}

