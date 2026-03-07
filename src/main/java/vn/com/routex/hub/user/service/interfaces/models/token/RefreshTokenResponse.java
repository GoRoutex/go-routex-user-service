package vn.com.routex.hub.user.service.interfaces.models.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RefreshTokenResponse extends BaseResponse<RefreshTokenResponse.RefreshTokenResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    public static class RefreshTokenResponseData {
        private String accessToken;
        private String refreshToken;
        private String userId;
        private OffsetDateTime accessTokenExpiredAt;
        private OffsetDateTime refreshTokenExpiredAt;
    }

}
