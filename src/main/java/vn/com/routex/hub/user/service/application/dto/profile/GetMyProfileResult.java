package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record GetMyProfileResult(
        String userId,
        String email,
        String phone,
        UserStatus status,
        Gender gender,
        String avatarUrl,
        String address,
        String nationalId,
        Boolean emailVerified,
        Boolean phoneVerified,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<String> authorities,
        MyCustomerProfileResult customer
) {
    @Builder
    public record MyCustomerProfileResult(
            String customerId,
            String fullName,
            BigDecimal tripPoints,
            Integer totalTrips,
            BigDecimal totalSpent,
            OffsetDateTime lastTripAt,
            OffsetDateTime lastBookingAt
    ) {
    }
}

