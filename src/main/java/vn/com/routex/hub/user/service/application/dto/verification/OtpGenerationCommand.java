package vn.com.routex.hub.user.service.application.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpGenerationCommand {

    private RequestContext context;
    private String userId;
    private String phoneNumber;
    private String email;
    private OtpPurpose purpose;
}
