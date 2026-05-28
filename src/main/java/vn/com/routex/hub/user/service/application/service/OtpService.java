package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.command.common.RequestContext;
import vn.com.routex.hub.user.service.application.command.verification.OtpGenerationResult;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.UserEvent;

public interface OtpService {
    OtpGenerationResult generateOtpAndSendMail(RequestContext context, UserEvent user, OtpPurpose otpPurpose);

    /**
     * Generates OTP and sends email in a new transaction so it can still succeed even if
     * the caller transaction is rolled back (e.g. re-registering while still VERIFYING).
     */
    OtpGenerationResult generateOtpAndSendMailRequiresNew(RequestContext context, UserEvent user, OtpPurpose otpPurpose);
}
