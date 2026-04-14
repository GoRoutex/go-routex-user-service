package vn.com.routex.hub.user.service.interfaces.models.profile;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.membership.model.MembershipBadge;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetMyProfileResponse {
    private ApiResult result;
    private GetMyProfileResponseData data;
    private GetMyMembershipResponseData membership;
    private GetMyMembershipStats stats;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetMyMembershipResponseData {
        private BigDecimal currentPoint;
        private Integer discountPercent;
        private Integer priorityLevel;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetMyMembershipStats {
        private Integer totalTrips;
        private String badge;
        private BigDecimal totalSpent;
        private BigDecimal pointToNextTier;
        private BigDecimal pointMultiplier;
        private MembershipBadge nextTierName;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetMyProfileResponseData {
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
        private Set<String> authorities;
        private MyCustomerProfile customer;
        private Set<String> roles;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class MyCustomerProfile {
        private String customerId;
        private String fullName;
        private BigDecimal tripPoints;
        private Integer totalTrips;
        private BigDecimal totalSpent;
        private OffsetDateTime lastTripAt;
        private OffsetDateTime lastBookingAt;
    }
}
