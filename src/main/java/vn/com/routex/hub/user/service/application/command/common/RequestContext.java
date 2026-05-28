package vn.com.routex.hub.user.service.application.command.common;

import lombok.Builder;

@Builder
public record RequestContext(
        String requestId,
        String requestDateTime,
        String channel,
        String merchantId,
        String userEmail,
        String userPhone
) {
}

