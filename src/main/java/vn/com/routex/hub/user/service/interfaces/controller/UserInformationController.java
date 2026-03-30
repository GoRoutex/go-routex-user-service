package vn.com.routex.hub.user.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.UpdateProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.UpdateProfileResult;
import vn.com.routex.hub.user.service.application.service.UserProfileService;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.CompleteProfileRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.CompleteProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetMyProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetMyProfileResponse.GetMyMembershipResponseData;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetMyProfileResponse.GetMyMembershipStats;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.UpdateProfileRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.UpdateProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.UpdateProfileResponse.UpdateProfileResponseData;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.COMPLETE_PROFILE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.ME_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.PROFILE_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.USER_SERVICE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + USER_SERVICE)
@RequiredArgsConstructor
public class UserInformationController {

    private final UserProfileService userProfileService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @GetMapping(PROFILE_PATH + ME_PATH)
    public ResponseEntity<GetMyProfileResponse> getMyProfile(
            @RequestParam String userId) {
        GetMyProfileResult result = userProfileService.getMyProfile(GetMyProfileCommand
                .builder()
                .userId(userId)
                .build());

        GetMyMembershipResponseData myMembership = GetMyMembershipResponseData.builder()
                .currentPoint(result.membership().currentPoint())
                .discountPercent(result.membership().discountPercent())
                .priorityLevel(result.membership().priorityLevel())
                .build();

        GetMyMembershipStats myStats = GetMyMembershipStats.builder()
                .totalTrips(result.stats().totalTrips())
                .badge(result.stats().badge())
                .totalSpent(result.stats().totalSpent())
                .pointToNextTier(result.stats().pointToNextTier())
                .pointMultiplier(result.stats().pointMultiplier())
                .nextTierName(result.stats().nextTierName())
                .build();

        GetMyProfileResponse.MyCustomerProfile myProfile = GetMyProfileResponse.MyCustomerProfile.builder()
                .customerId(result.customer().customerId())
                .fullName(result.customer().fullName())
                .tripPoints(result.customer().tripPoints())
                .totalTrips(result.customer().totalTrips())
                .totalSpent(result.customer().totalSpent())
                .lastTripAt(result.customer().lastTripAt())
                .lastBookingAt(result.customer().lastBookingAt())
                .build();

        return ResponseEntity.ok(GetMyProfileResponse.builder()
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(GetMyProfileResponse.GetMyProfileResponseData
                        .builder()
                        .userId(result.userId())
                        .email(result.email())
                        .phone(result.phone())
                        .status(result.status())
                        .gender(result.gender())
                        .avatarUrl(result.avatarUrl())
                        .address(result.address())
                        .nationalId(result.nationalId())
                        .emailVerified(result.emailVerified())
                        .phoneVerified(result.phoneVerified())
                        .createdAt(result.createdAt())
                        .updatedAt(result.updatedAt())
                        .authorities(result.authorities())
                        .customer(myProfile)
                        .build())
                .membership(myMembership)
                .stats(myStats)
                .build());
    }

    @PostMapping(PROFILE_PATH)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('profile:view')")
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
                        .userId(result.userId())
                        .username(result.username())
                        .email(result.email())
                        .phone(result.phone())
                        .fullName(result.fullName())
                        .status(result.status())
                        .emailVerified(result.emailVerified())
                        .phoneVerified(result.phoneVerified())
                        .createdAt(result.createdAt())
                        .updatedAt(result.updatedAt())
                        .build())
                .build());
    }


    @PostMapping(PROFILE_PATH + UPDATE_PATH)
    public ResponseEntity<UpdateProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {


        sLog.info("[UPDATE-PROFILE] Update Profile Request: {}", request);
        UpdateProfileResult result = userProfileService.updateProfile(
                UpdateProfileCommand.builder()
                        .context(toContext(request))
                        .userId(request.getUserId())
                        .fullName(request.getData().getFullName())
                        .email(request.getData().getEmail())
                        .phoneNumber(request.getData().getPhoneNumber())
                        .address(request.getData().getAddress()).build());

        UpdateProfileResponse response = UpdateProfileResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(UpdateProfileResponseData.builder()
                        .userId(result.userId())
                        .fullName(result.fullName())
                        .address(result.address())
                        .email(result.email())
                        .phoneNumber(result.phoneNumber()).build())
                .build();

        sLog.info("[UPDATE-PROFILE] Update Profile Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(PROFILE_PATH + COMPLETE_PROFILE)
    public ResponseEntity<CompleteProfileResponse> completeProfile(@Valid @RequestBody CompleteProfileRequest request) {
        sLog.info("[COMPLETE-PROFILE] Complete Profile Request: {}", request);
        CompleteProfileResult result = userProfileService.completeProfile(
                CompleteProfileCommand.builder()
                        .context(toContext(request))
                        .userId(request.getData().getUserId())
                        .fullName(request.getData().getFullName())
                        .address(request.getData().getAddress())
                        .avatarUrl(request.getData().getAvatarUrl())
                        .nationalId(request.getData().getNationalId())
                        .gender(request.getData().getGender())
                        .build());

        CompleteProfileResponse response = CompleteProfileResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(CompleteProfileResponse.CompleteProfileResponseData.builder()
                        .userId(result.userId())
                        .fullName(result.fullName())
                        .gender(result.gender())
                        .avatarUrl(result.avatarUrl())
                        .profileCompleted(result.profileCompleted())
                        .address(result.address())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    private RequestContext toContext(BaseRequest request) {
        return RequestContext.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }
}
