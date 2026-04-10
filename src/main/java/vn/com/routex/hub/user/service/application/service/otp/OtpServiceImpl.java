package vn.com.routex.hub.user.service.application.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public OtpGenerationResult generateOtpAndSendMail(RequestContext context, UserEvent user, OtpPurpose otpPurpose) {
        return doGenerateOtpAndSendMail(context, user, otpPurpose);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OtpGenerationResult generateOtpAndSendMailRequiresNew(RequestContext context, UserEvent user, OtpPurpose otpPurpose) {
        return doGenerateOtpAndSendMail(context, user, otpPurpose);
    }

    private OtpGenerationResult doGenerateOtpAndSendMail(RequestContext context, UserEvent user, OtpPurpose otpPurpose) {
        sLog.info("Generating OTP and sending Email");
        OtpGenerationResult otpResult = verificationService.createClientOtp(OtpGenerationCommand.builder()
                .context(context)
                .userId(user.userId())
                .email(user.email())
                .phoneNumber(user.phoneNumber())
                .purpose(otpPurpose)
                .build());

        sLog.info("OTP: {}", otpResult);
        emailService.sendEmail(EmailMessageCommand.builder()
                .toEmail(otpResult.email())
                .userId(otpResult.userId())
                .fullName(otpResult.fullName())
                .verificationCode(otpResult.plainOtp())
                .expireMinutes(otpResult.expiresMinutes())
                .purpose(otpPurpose)
                .build());
        return otpResult;
    }
}
