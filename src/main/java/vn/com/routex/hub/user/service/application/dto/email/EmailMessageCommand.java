package vn.com.routex.hub.user.service.application.dto.email;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;

@Builder
public record EmailMessageCommand(
        String toEmail,
        String fullName,
        String verificationCode,
        Long expireMinutes,
        String userId,
        OtpPurpose purpose
) {
}

