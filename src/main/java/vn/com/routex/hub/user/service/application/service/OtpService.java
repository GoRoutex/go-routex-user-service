package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationResult;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.UserEvent;

public interface OtpService {
    OtpGenerationResult generateOtpAndSendMail(RequestContext context, UserEvent user, OtpPurpose otpPurpose);
}
