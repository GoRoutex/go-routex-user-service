package vn.com.routex.hub.user.service.application.command.authentication;

import lombok.Builder;

@Builder
public record ResetPasswordResult(
        String userId
) {
}
