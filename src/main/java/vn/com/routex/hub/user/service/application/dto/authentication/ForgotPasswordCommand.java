package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Builder
public record ForgotPasswordCommand(
        RequestContext context,
        String username,
        String email
) {
}

