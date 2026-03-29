package vn.com.routex.hub.user.service.application.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationCommand;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationResult;
import vn.com.routex.hub.user.service.application.service.VerificationService;
import vn.com.routex.hub.user.service.domain.otp.model.Otp;
import vn.com.routex.hub.user.service.domain.otp.model.OtpStatus;
import vn.com.routex.hub.user.service.domain.otp.port.OtpRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.BusinessConstant.EXPIRED_OTP_MINUTES;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.BusinessConstant.OTP_LENGTH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.BusinessConstant.RESEND_COOLDOWN_SECONDS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OTP_COOL_DOWN;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OTP_COOL_DOWN_MESSAGE;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final OtpRepositoryPort otpRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OtpGenerationResult createClientOtp(OtpGenerationCommand command) {
        otpRepositoryPort.findLatestActiveOtp(command.getUserId(), command.getPurpose())
            .ifPresent(existing -> {
                if (existing.getProducedAt() != null) {
                    long seconds = Duration.between(existing.getProducedAt(), OffsetDateTime.now()).getSeconds();
                    if (seconds < RESEND_COOLDOWN_SECONDS) {
                        throw new BusinessException(
                                command.getContext().getRequestId(),
                                command.getContext().getRequestDateTime(),
                                command.getContext().getChannel(),
                                ExceptionUtils.buildResultResponse(OTP_COOL_DOWN, OTP_COOL_DOWN_MESSAGE)
                        );
                    }
                }

                existing.setStatus(OtpStatus.REVOKED);
                existing.setUpdatedAt(OffsetDateTime.now());
                existing.setExpiredAt(OffsetDateTime.now());
                otpRepositoryPort.save(existing);
            });

        String plainOtp = generateOtp();
        Otp otp = Otp.builder()
                .id(UUID.randomUUID().toString())
                .userId(command.getUserId())
                .phoneNumber(command.getPhoneNumber())
                .email(command.getEmail())
                .purpose(command.getPurpose())
                .expiredAt(OffsetDateTime.now().plusMinutes(EXPIRED_OTP_MINUTES))
                .producedAt(OffsetDateTime.now())
                .otpHash(passwordEncoder.encode(plainOtp))
                .status(OtpStatus.ACTIVE)
                .attemptCount(0)
                .createdAt(OffsetDateTime.now())
                .build();

        sLog.info("OTP Created: {}", otp);

        otpRepositoryPort.save(otp);

        sLog.info("OTP Saved");

        long expiresMinutes = ChronoUnit.MINUTES.between(OffsetDateTime.now(), otp.getExpiredAt());
        return OtpGenerationResult.builder()
                .plainOtp(plainOtp)
                .userId(command.getUserId())
                .email(command.getEmail())
                .expiredAt(otp.getExpiredAt())
                .expiresMinutes(expiresMinutes)
                .build();
    }

    private String generateOtp() {
        int bound = (int) Math.pow(10, OTP_LENGTH);
        int value = secureRandom.nextInt(bound);
        return String.format("%0" + OTP_LENGTH + "d", value);
    }
}
