package vn.com.routex.hub.user.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import vn.com.routex.hub.user.service.application.service.UserProfileService;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.CompleteProfileRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.CompleteProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetMyProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileRequest;
import vn.com.routex.hub.user.service.interfaces.models.profile.GetUserProfileResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.COMPLETE_PROFILE;
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

        GetMyProfileResponse.MyCustomerProfile myProfile = GetMyProfileResponse.MyCustomerProfile.builder()
                .customerId(result.getCustomer().getCustomerId())
                .fullName((result.getCustomer().getFullName()))
                .tripPoints(result.getCustomer().getTripPoints())
                .totalTrips(result.getCustomer().getTotalTrips())
                .totalSpent(result.getCustomer().getTotalSpent())
                .lastTripAt(result.getCustomer().getLastTripAt())
                .lastBookingAt(result.getCustomer().getLastBookingAt())
                .build();

        return ResponseEntity.ok(GetMyProfileResponse.builder()
                        .result(ApiResult.builder()
                                .responseCode(SUCCESS_CODE)
                                .description(SUCCESS_MESSAGE)
                                .build())
                .data(GetMyProfileResponse.GetMyProfileResponseData
                        .builder()
                        .userId(result.getUserId())
                        .email(result.getEmail())
                        .phone(result.getPhone())
                        .status(result.getStatus())
                        .gender(result.getGender())
                        .avatarUrl(result.getAvatarUrl())
                        .address(result.getAddress())
                        .nationalId(result.getNationalId())
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
                        .userId(result.getUserId())
                        .fullName(result.getFullName())
                        .gender(result.getGender())
                        .avatarUrl(result.getAvatarUrl())
                        .profileCompleted(result.getProfileCompleted())
                        .address(result.getAddress())
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
