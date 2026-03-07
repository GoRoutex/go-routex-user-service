package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.domain.otp.OtpPurpose;
import vn.com.routex.hub.user.service.interfaces.models.otp.OtpRequest;
import vn.com.routex.hub.user.service.interfaces.models.otp.OtpResponse;

public interface VerificationService {

    OtpResponse createClientOtp(OtpRequest request, OtpPurpose otpPurpose);
}
