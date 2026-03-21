package vn.com.routex.hub.user.service.domain.otp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Otp extends AbstractAuditingEntity {

    private String id;

    private String userId;

    private String phoneNumber;

    private String email;

    private String fullName;

    private OtpPurpose purpose;

    private OtpStatus status;

    private String otpHash;

    private OffsetDateTime producedAt;

    private Integer attemptCount = 0;

    private OffsetDateTime expiredAt;

    private OffsetDateTime consumedAt;
}
