package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserProfileResult {

    private String userId;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private UserStatus status;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private List<String> authorities;
    private CustomerProfileResult customer;

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    @AllArgsConstructor
    public static class CustomerProfileResult {
        private String customerId;
        private BigDecimal tripPoints;
        private Integer totalTrips;
        private BigDecimal totalSpent;
        private OffsetDateTime lastTripAt;
    }
}
