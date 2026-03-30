package vn.com.routex.hub.user.service.application.dto.common;

import lombok.Builder;

@Builder
public record RequestContext(
        String requestId,
        String requestDateTime,
        String channel
) {
}

