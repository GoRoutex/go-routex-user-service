package vn.com.routex.hub.user.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;

@Builder
public record SearchUserQuery(
        RequestContext context,
        String keyword,
        String excludeRole,
        int page,
        int size
) {
}
