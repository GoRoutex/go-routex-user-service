package vn.com.routex.hub.user.service.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.user.service.application.dto.authentication.ChangePasswordCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.ChangePasswordResult;
import vn.com.routex.hub.user.service.application.dto.authentication.ForgotPasswordCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.ForgotPasswordResult;
import vn.com.routex.hub.user.service.application.dto.authentication.LoginCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.LoginResult;
import vn.com.routex.hub.user.service.application.dto.authentication.LogoutCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RegistrationCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RegistrationResult;
import vn.com.routex.hub.user.service.application.dto.authentication.VerifyOtpCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.VerifyOtpResult;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.service.AuthenticationService;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.login.LoginRequest;
import vn.com.routex.hub.user.service.interfaces.models.login.LoginResponse;
import vn.com.routex.hub.user.service.interfaces.models.logout.LogoutRequest;
import vn.com.routex.hub.user.service.interfaces.models.logout.LogoutResponse;
import vn.com.routex.hub.user.service.interfaces.models.password.ChangePasswordRequest;
import vn.com.routex.hub.user.service.interfaces.models.password.ChangePasswordResponse;
import vn.com.routex.hub.user.service.interfaces.models.password.ForgotPasswordRequest;
import vn.com.routex.hub.user.service.interfaces.models.password.ForgotPasswordResponse;
import vn.com.routex.hub.user.service.interfaces.models.register.RegistrationRequest;
import vn.com.routex.hub.user.service.interfaces.models.register.RegistrationResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.user.service.interfaces.models.verify.VerifyCodeRequest;
import vn.com.routex.hub.user.service.interfaces.models.verify.VerifyCodeResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.AUTHENTICATION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.CHANGE_PASSWORD;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.FORGOT_PASSWORD;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.LOGIN;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.LOGOUT;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.REGISTER;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.USER_SERVICE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.VERIFY_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + USER_SERVICE)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(AUTHENTICATION + REGISTER)
    public ResponseEntity<RegistrationResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResult result = authenticationService.registerUser(RegistrationCommand.builder()
                .context(toContext(request))
                .username(request.getData().getUsername())
                .password(request.getData().getPassword())
                .email(request.getData().getEmail())
                .phoneNumber(request.getData().getPhoneNumber())
                .fullName(request.getData().getFullName())
                .dob(request.getData().getDob())
                .language(request.getData().getLanguage())
                .tenantId(request.getData().getTenantId())
                .timeZone(request.getData().getTimeZone())
                .build());

        return ResponseEntity.ok(RegistrationResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(successResult())
                .data(RegistrationResponse.RegistrationResponseData.builder()
                        .userId(result.getUserId())
                        .email(result.getEmail())
                        .phoneNumber(result.getPhoneNumber())
                        .userName(result.getUserName())
                        .status(result.getStatus())
                        .build())
                .build());
    }

    @PostMapping(AUTHENTICATION + VERIFY_CODE)
    public ResponseEntity<VerifyCodeResponse> verifyOtpCode(@Valid @RequestBody VerifyCodeRequest request) {
        VerifyOtpResult result = authenticationService.verifyOtpUser(VerifyOtpCommand.builder()
                .context(toContext(request))
                .userId(request.getData().getUserId())
                .otpCode(request.getData().getOtpCode())
                .phoneNumber(request.getData().getPhoneNumber())
                .email(request.getData().getEmail())
                .purpose(request.getData().getPurpose())
                .build());

        return ResponseEntity.ok(VerifyCodeResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(successResult())
                .data(VerifyCodeResponse.VerifyCodeResponseData.builder()
                        .userId(result.getUserId())
                        .otpCode(result.getOtpCode())
                        .status(result.getStatus())
                        .build())
                .build());
    }

    @PostMapping(AUTHENTICATION + LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = authenticationService.login(LoginCommand.builder()
                .context(toContext(request))
                .username(request.getData().getUsername())
                .password(request.getData().getPassword())
                .build());

        return ResponseEntity.ok(LoginResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(successResult())
                .data(LoginResponse.LoginResponseData.builder()
                        .accessToken(result.getAccessToken())
                        .refreshToken(result.getRefreshToken())
                        .userId(result.getUserId())
                        .username(result.getUsername())
                        .roles(result.getRoles())
                        .authorities(result.getAuthorities())
                        .build())
                .build());
    }

    @PostMapping(AUTHENTICATION + CHANGE_PASSWORD)
    public ResponseEntity<ChangePasswordResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        ChangePasswordResult result = authenticationService.changePassword(ChangePasswordCommand.builder()
                .context(toContext(request))
                .userId(request.getData().getUserId())
                .oldPassword(request.getData().getOldPassword())
                .newPassword(request.getData().getNewPassword())
                .confirmNewPassword(request.getData().getConfirmNewPassword())
                .build());

        return ResponseEntity.ok(ChangePasswordResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(successResult())
                .data(ChangePasswordResponse.ChangePasswordResponseData.builder()
                        .userId(result.getUserId())
                        .changeAt(result.getChangeAt())
                        .build())
                .build());
    }

    @PostMapping(AUTHENTICATION + FORGOT_PASSWORD)
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResult result = authenticationService.forgotPassword(ForgotPasswordCommand.builder()
                .context(toContext(request))
                .username(request.getData().getUsername())
                .email(request.getData().getEmail())
                .build());

        return ResponseEntity.ok(ForgotPasswordResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(successResult())
                .data(ForgotPasswordResponse.ForgotPasswordResponseData.builder()
                        .userId(result.getUserId())
                        .expiresMinutes(result.getExpiresMinutes())
                        .build())
                .build());
    }

    @PostMapping(AUTHENTICATION + LOGOUT)
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(LogoutCommand.builder()
                .context(toContext(request))
                .refreshToken(request.getData().getRefreshToken())
                .build());

        return ResponseEntity.ok(LogoutResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(successResult())
                .build());
    }

    private RequestContext toContext(BaseRequest request) {
        return RequestContext.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }

    private ApiResult successResult() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }
}
