package vn.com.routex.hub.user.service.application.dto.verification;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResendVerificationResult {
    private long expiresAfterSeconds;
    private long retryAfterSeconds;
}
