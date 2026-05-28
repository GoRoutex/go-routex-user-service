package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.command.authentication.ChangePasswordCommand;
import vn.com.routex.hub.user.service.application.command.authentication.ChangePasswordResult;
import vn.com.routex.hub.user.service.application.command.authentication.ForgotPasswordCommand;
import vn.com.routex.hub.user.service.application.command.authentication.ForgotPasswordResult;
import vn.com.routex.hub.user.service.application.command.authentication.LoginCommand;
import vn.com.routex.hub.user.service.application.command.authentication.LoginResult;
import vn.com.routex.hub.user.service.application.command.authentication.LogoutCommand;
import vn.com.routex.hub.user.service.application.command.authentication.RefreshTokenCommand;
import vn.com.routex.hub.user.service.application.command.authentication.RefreshTokenResult;
import vn.com.routex.hub.user.service.application.command.authentication.RegistrationCommand;
import vn.com.routex.hub.user.service.application.command.authentication.RegistrationResult;
import vn.com.routex.hub.user.service.application.command.authentication.ResetPasswordCommand;
import vn.com.routex.hub.user.service.application.command.authentication.ResetPasswordResult;
import vn.com.routex.hub.user.service.application.command.authentication.VerifyOtpCommand;
import vn.com.routex.hub.user.service.application.command.authentication.VerifyOtpResult;
import vn.com.routex.hub.user.service.application.command.verification.ResendVerificationCommand;
import vn.com.routex.hub.user.service.application.command.verification.ResendVerificationResult;

public interface AuthenticationService {

    RegistrationResult registerUser(RegistrationCommand command);

    VerifyOtpResult verifyOtpUser(VerifyOtpCommand command);

    LoginResult login(LoginCommand command);

    ChangePasswordResult changePassword(ChangePasswordCommand command);

    ForgotPasswordResult forgotPassword(ForgotPasswordCommand command);

    RefreshTokenResult refreshToken(RefreshTokenCommand command);

    void logout(LogoutCommand command);

    ResendVerificationResult resendVerificationCode(ResendVerificationCommand build);

    ResetPasswordResult resetPassword(ResetPasswordCommand command);
}
