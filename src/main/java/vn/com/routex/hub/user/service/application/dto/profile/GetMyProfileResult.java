package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyProfileResult {

    private String userId;
    private String email;
    private String phone;
    private UserStatus status;
    private Gender gender;
    private String avatarUrl;
    private String address;
    private String nationalId;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private List<String> authorities;
    private MyCustomerProfileResult customer;

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    @AllArgsConstructor
    public static class MyCustomerProfileResult {
        private String customerId;
        private String fullName;
        private BigDecimal tripPoints;
        private Integer totalTrips;
        private BigDecimal totalSpent;
        private OffsetDateTime lastTripAt;
        private OffsetDateTime lastBookingAt;
    }
}
