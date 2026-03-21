package vn.com.routex.hub.user.service.application.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpGenerationResult {

    private String plainOtp;
    private String fullName;
    private String email;
    private String userId;
    private Long expiresMinutes;
}
