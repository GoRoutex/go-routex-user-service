package vn.com.routex.hub.user.service.application.command.verification;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;

@Builder
public record ResendVerificationCommand(
        RequestContext context,
        String email,
        OtpPurpose otpPurpose
) {
}

