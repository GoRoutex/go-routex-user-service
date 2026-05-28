package vn.com.routex.hub.user.service.application.command.authentication;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ChangePasswordResult(
        String userId,
        OffsetDateTime changeAt
) {
}

