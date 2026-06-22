package vn.com.routex.hub.user.service.application.command.user;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchUserResult(
        List<SearchUserItemResult> data
) {
    @Builder
    public record SearchUserItemResult(
            String userId,
            String fullName,
            String phoneNumber,
            String email,
            String avatarUrl
    ) {
    }
}
