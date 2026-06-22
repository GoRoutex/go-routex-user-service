package vn.com.routex.hub.user.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;

@Builder
public record FetchUserDetailQuery(
        RequestContext context,
        String userId
) {
}
