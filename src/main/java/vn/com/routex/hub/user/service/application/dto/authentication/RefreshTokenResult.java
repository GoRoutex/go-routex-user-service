package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResult {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private OffsetDateTime accessTokenExpiredAt;
    private OffsetDateTime refreshTokenExpiredAt;
}
