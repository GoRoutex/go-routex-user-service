package vn.com.routex.hub.user.service.application.dto.membership;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.membership.model.MembershipBadge;

import java.math.BigDecimal;

@Builder
public record GetMyMembershipResult(
        String userId,
        BigDecimal currentPoint,
        MyMembershipBenefitResult benefit
) {
    @Builder
    public record MyMembershipBenefitResult(
            String badge,
            Integer discountPercent,
            Integer priorityLevel,
            BigDecimal pointToNextTier,
            BigDecimal pointMultiplier,
            BigDecimal totalSpent,
            Integer totalTrips,
            MembershipBadge nextTierName
    ) {
    }
}

