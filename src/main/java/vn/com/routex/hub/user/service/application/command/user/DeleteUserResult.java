package vn.com.routex.hub.user.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;

@Builder
public record DeleteUserResult(
        String id,
        UserStatus status
) {
}
