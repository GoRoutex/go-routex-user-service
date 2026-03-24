package vn.com.routex.hub.user.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipCommand;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileResult;
import vn.com.routex.hub.user.service.application.service.UserProfileService;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.membership.GetMyMembershipResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetMyProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileResponse;
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
public class UserInformationController {

    private final UserProfileService userProfileService;


    @GetMapping(PROFILE_PATH + ME_PATH + MEMBERSHIP_PATH)
    public ResponseEntity<GetMyMembershipResponse> getMyMembership(@RequestParam String userId) {

        GetMyMembershipResult result = userProfileService.getMyMembership(GetMyMembershipCommand
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
    @GetMapping(PROFILE_PATH + ME_PATH)
    public ResponseEntity<GetMyProfileResponse> getMyProfile(
            @RequestParam String userId) {
        GetMyProfileResult result = userProfileService.getMyProfile(GetMyProfileCommand
                .builder()
                .userId(userId)
                .build());

        GetMyProfileResponse.MyCustomerProfile myProfile = GetMyProfileResponse.MyCustomerProfile.builder()
                .customerId(result.getCustomer().getCustomerId())
                .tripPoints(result.getCustomer().getTripPoints())
                .totalTrips(result.getCustomer().getTotalTrips())
                .totalSpent(result.getCustomer().getTotalSpent())
                .lastTripAt(result.getCustomer().getLastTripAt())
                .build();

        return ResponseEntity.ok(GetMyProfileResponse.builder()
                        .result(ApiResult.builder()
                                .responseCode(SUCCESS_CODE)
                                .description(SUCCESS_MESSAGE)
                                .build())
                .data(GetMyProfileResponse.GetMyProfileResponseData
                        .builder()
                        .userId(result.getUserId())
                        .username(result.getUsername())
                        .email(result.getEmail())
                        .phone(result.getPhone())
                        .fullName(result.getPhone())
                        .status(result.getStatus())
                        .emailVerified(result.getEmailVerified())
                        .phoneVerified(result.getPhoneVerified())
                        .createdAt(result.getCreatedAt())
                        .updatedAt(result.getUpdatedAt())
                        .authorities(result.getAuthorities())
                        .customer(myProfile)
                        .build())
                .build());
    }

    @PostMapping(PROFILE_PATH)
    public ResponseEntity<GetUserProfileResponse> getProfiles(@Valid @RequestBody GetUserProfileRequest request) {
        GetUserProfileResult result = userProfileService.getUserProfile(GetUserProfileCommand.builder()
                        .context(toContext(request))
                        .userId(request.getData().getUserId())
                        .build());


        return ResponseEntity.ok(GetUserProfileResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(GetUserProfileResponse.GetUserProfileResponseData
                        .builder()
                        .userId(result.getUserId())
                        .username(result.getUsername())
                        .email(result.getEmail())
                        .phone(result.getPhone())
                        .fullName(result.getFullName())
                        .status(result.getStatus())
                        .emailVerified(result.getEmailVerified())
                        .phoneVerified(result.getPhoneVerified())
                        .createdAt(result.getCreatedAt())
                        .updatedAt(result.getUpdatedAt())
                        .build())
                .build());
    }

    private RequestContext toContext(BaseRequest request) {
        return RequestContext.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }
}
