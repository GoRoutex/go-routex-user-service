package vn.com.routex.hub.user.service.application.command.authentication;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;

@Builder
public record LoginCommand(
        RequestContext context,
        String email,
        String password
) {
}

