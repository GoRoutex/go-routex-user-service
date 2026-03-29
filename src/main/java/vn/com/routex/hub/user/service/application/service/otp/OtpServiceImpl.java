package vn.com.routex.hub.user.service.application.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.email.EmailMessageCommand;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationCommand;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationResult;
import vn.com.routex.hub.user.service.application.service.EmailService;
import vn.com.routex.hub.user.service.application.service.OtpService;
import vn.com.routex.hub.user.service.application.service.VerificationService;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.UserEvent;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;

@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final EmailService emailService;
    private final VerificationService verificationService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public OtpGenerationResult generateOtpAndSendMail(RequestContext context, UserEvent user, OtpPurpose otpPurpose) {
        sLog.info("Generating OTP and sending Email");
        OtpGenerationResult otpResult = verificationService.createClientOtp(OtpGenerationCommand.builder()
                .context(context)
                .userId(user.userId())
                .email(user.email())
                .phoneNumber(user.phoneNumber())
                .purpose(otpPurpose)
                .build());

        sLog.info("OTP: {}", otpResult);
        sLog.info("Sending Email");
        emailService.sendEmail(EmailMessageCommand.builder()
                .toEmail(otpResult.getEmail())
                .userId(otpResult.getUserId())
                .fullName(otpResult.getFullName())
                .verificationCode(otpResult.getPlainOtp())
                .expireMinutes(otpResult.getExpiresMinutes())
                .build());


        return otpResult;
    }
}
