package vn.com.routex.hub.user.service.application.dto.membership;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.membership.model.MembershipBadge;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetMyMembershipResult {
    private String userId;
    private BigDecimal currentPoint;
    private MyMembershipBenefitResult benefit;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class MyMembershipBenefitResult {
        private String badge;
        private Integer discountPercent;
        private Integer priorityLevel;
        private BigDecimal pointToNextTier;
        private BigDecimal pointMultiplier;
        private BigDecimal totalSpent;
        private Integer totalTrips;
        private MembershipBadge nextTierName;
    }
}
