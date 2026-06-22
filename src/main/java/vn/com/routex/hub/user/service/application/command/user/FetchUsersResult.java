package vn.com.routex.hub.user.service.application.command.user;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record FetchUsersResult(
        List<FetchUserItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
    @Builder
    public record FetchUserItemResult(
            String id,
            String email,
            String fullName,
            String phoneNumber,
            String avatarUrl,
            Set<String> roles,
            LocalDate dob,
            Gender gender,
            Boolean phoneVerified,
            Boolean profileCompleted,
            Boolean emailVerified,
            UserStatus status,
            String language,
            String timezone,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
    }
}
