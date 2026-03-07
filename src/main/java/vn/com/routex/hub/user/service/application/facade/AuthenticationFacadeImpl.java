package vn.com.routex.hub.user.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.service.AuthenticationService;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;
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
import vn.com.routex.hub.user.service.interfaces.models.token.RefreshTokenRequest;
import vn.com.routex.hub.user.service.interfaces.models.token.RefreshTokenResponse;
import vn.com.routex.hub.user.service.interfaces.models.verify.VerifyCodeRequest;
import vn.com.routex.hub.user.service.interfaces.models.verify.VerifyCodeResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<RegistrationResponse> registerUser(RegistrationRequest request) {
        RegistrationResponse response = authenticationService.registerUser(request);
        return buildResponse(request, response);
    }

    @Override
    public ResponseEntity<VerifyCodeResponse> verifyOtpCode(VerifyCodeRequest request) {
        VerifyCodeResponse response = authenticationService.verifyOtpUser(request);
        return buildResponse(request, response);
    }


    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        LoginResponse response = authenticationService.login(request);
        return buildResponse(request, response);
    }

    @Override
    public ResponseEntity<ChangePasswordResponse> changePassword(ChangePasswordRequest request) {
        ChangePasswordResponse response = authenticationService.changePassword(request);
        return buildResponse(request, response);
    }

    @Override
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request) {
        ForgotPasswordResponse response = authenticationService.forgotPassword(request);
        return buildResponse(request, response);
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        RefreshTokenResponse response = authenticationService.refreshToken(request);
        return buildResponse(request, response);
    }

    @Override
    public ResponseEntity<LogoutResponse> logout(LogoutRequest request) {
        LogoutResponse response = authenticationService.logout(request);
        if(response == null) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
        response.setRequestId(request.getRequestId());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setChannel(request.getChannel());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private <T, R extends BaseResponse<T>> ResponseEntity<R> buildResponse(BaseRequest request, R response) {
        if (response == null) {
            throw new BusinessException(
                    request.getRequestId(),
                    request.getRequestDateTime(),
                    request.getChannel(),
                    ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE)
            );
        }

        response.setRequestId(request.getRequestId());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setChannel(request.getChannel());

        return ResponseEntity
                .status(response.getData() == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
                .body(response);
    }
}
