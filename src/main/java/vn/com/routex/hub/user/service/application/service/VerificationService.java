package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationCommand;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationResult;

public interface VerificationService {

    OtpGenerationResult createClientOtp(OtpGenerationCommand command);
}
