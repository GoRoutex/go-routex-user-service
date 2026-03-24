package vn.com.routex.hub.user.service.interfaces.models.membership;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.membership.model.MembershipBadge;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetMyMembershipResponse {
    private ApiResult result;
    private GetMyMembershipResponseData data;
    private GetMyMembershipStats stats;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetMyMembershipResponseData {
        private String userId;
        private BigDecimal currentPoint;
        private Integer discountPercent;
        private String benefits;
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
}
