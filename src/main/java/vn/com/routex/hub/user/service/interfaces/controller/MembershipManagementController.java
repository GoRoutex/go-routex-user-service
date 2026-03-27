package vn.com.routex.hub.user.service.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipCommand;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipResult;
import vn.com.routex.hub.user.service.application.service.MembershipProfileService;
import vn.com.routex.hub.user.service.interfaces.models.membership.GetMyMembershipResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.MEMBERSHIP_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.ME_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.PROFILE_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.USER_SERVICE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + USER_SERVICE)
@RequiredArgsConstructor
public class MembershipManagementController {

    private final MembershipProfileService membershipProfileService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @GetMapping(PROFILE_PATH + ME_PATH + MEMBERSHIP_PATH)
    public ResponseEntity<GetMyMembershipResponse> getMyMembership(@RequestParam String userId) {

        GetMyMembershipResult result = membershipProfileService.getMyMembership(GetMyMembershipCommand
                .builder()
                .userId(userId)
                .build());

        return ResponseEntity.ok(GetMyMembershipResponse
                .builder()
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(GetMyMembershipResponse.GetMyMembershipResponseData
                        .builder()
                        .userId(userId)
                        .currentPoint(result.getCurrentPoint())
                        .discountPercent(result.getBenefit().getDiscountPercent())
                        .priorityLevel(result.getBenefit().getPriorityLevel())
                        .build())
                .stats(GetMyMembershipResponse.GetMyMembershipStats
                        .builder()
                        .badge(result.getBenefit().getBadge())
                        .totalTrips(result.getBenefit().getTotalTrips())
                        .totalSpent(result.getBenefit().getTotalSpent())
                        .pointMultiplier(result.getBenefit().getPointMultiplier())
                        .pointToNextTier(result.getBenefit().getPointToNextTier())
                        .nextTierName(result.getBenefit().getNextTierName())
                        .build())
                .build());

    }

}
