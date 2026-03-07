package vn.com.routex.hub.user.service.application.service;

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

public interface AuthenticationService {

    RegistrationResponse registerUser(RegistrationRequest request);
    VerifyCodeResponse verifyOtpUser(VerifyCodeRequest request);
    LoginResponse login(LoginRequest request);
    ChangePasswordResponse changePassword(ChangePasswordRequest request);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    LogoutResponse logout(LogoutRequest request);
}
