package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.dto.authentication.ChangePasswordCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.ChangePasswordResult;
import vn.com.routex.hub.user.service.application.dto.authentication.ForgotPasswordCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.ForgotPasswordResult;
import vn.com.routex.hub.user.service.application.dto.authentication.LoginCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.LoginResult;
import vn.com.routex.hub.user.service.application.dto.authentication.LogoutCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.LogoutResult;
import vn.com.routex.hub.user.service.application.dto.authentication.RefreshTokenCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RefreshTokenResult;
import vn.com.routex.hub.user.service.application.dto.authentication.RegistrationCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RegistrationResult;
import vn.com.routex.hub.user.service.application.dto.authentication.VerifyOtpCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.VerifyOtpResult;
import vn.com.routex.hub.user.service.application.dto.verification.ResendVerificationCommand;
import vn.com.routex.hub.user.service.application.dto.verification.ResendVerificationResult;

public interface AuthenticationService {

    RegistrationResult registerUser(RegistrationCommand command);

    VerifyOtpResult verifyOtpUser(VerifyOtpCommand command);

    LoginResult login(LoginCommand command);

    ChangePasswordResult changePassword(ChangePasswordCommand command);

    ForgotPasswordResult forgotPassword(ForgotPasswordCommand command);

    RefreshTokenResult refreshToken(RefreshTokenCommand command);

    void logout(LogoutCommand command);

    ResendVerificationResult resendVerificationCode(ResendVerificationCommand build);
}
