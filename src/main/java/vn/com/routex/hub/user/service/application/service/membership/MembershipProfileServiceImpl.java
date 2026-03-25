package vn.com.routex.hub.user.service.application.service.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.user.service.application.dto.customer.CustomerMembershipView;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipCommand;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipResult;
import vn.com.routex.hub.user.service.application.query.CustomerMembershipQueryRepository;
import vn.com.routex.hub.user.service.application.service.MembershipProfileService;
import vn.com.routex.hub.user.service.domain.membership.model.MembershipTier;
import vn.com.routex.hub.user.service.domain.membership.port.MembershipTierRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SPRING_DATA_NOT_FOUND_MESSAGE;


@Service
@RequiredArgsConstructor
public class MembershipProfileServiceImpl implements MembershipProfileService {


    private final CustomerMembershipQueryRepository customerMembershipQueryRepository;
    private final MembershipTierRepositoryPort membershipTierRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetMyMembershipResult getMyMembership(GetMyMembershipCommand command) {

        CustomerMembershipView customerMemberShipView = customerMembershipQueryRepository.findMembershipSummaryByUserId(command.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SPRING_DATA_NOT_FOUND_MESSAGE)));

        Optional<MembershipTier> nextTierOpt = membershipTierRepositoryPort.findByPriorityLevel(customerMemberShipView.priorityLevel() + 1);

        BigDecimal pointToNextTier = nextTierOpt
                .map(next -> next.getMinPoints().subtract(customerMemberShipView.tripPoints()).max(BigDecimal.ZERO))
                .orElse(BigDecimal.ZERO);

        return GetMyMembershipResult
                .builder()
                .userId(command.getUserId())
                .currentPoint(customerMemberShipView.tripPoints())
                .benefit(GetMyMembershipResult.MyMembershipBenefitResult.builder()
                        .badge(customerMemberShipView.currentBadge())
                        .discountPercent(customerMemberShipView.discountPercent())
                        .priorityLevel(customerMemberShipView.priorityLevel())
                        .pointToNextTier(pointToNextTier)
                        .pointMultiplier(customerMemberShipView.pointMultiplier())
                        .totalTrips(customerMemberShipView.totalTrips())
                        .totalSpent(customerMemberShipView.totalSpent())
                        .nextTierName(nextTierOpt.map(MembershipTier::getBadge).orElse(null))
                        .build()
                )
                .build();

    }
}
