package vn.com.routex.hub.user.service.application.dto.membership;

import lombok.Builder;

@Builder
public record GetMyMembershipCommand(
        String userId
) {
}

