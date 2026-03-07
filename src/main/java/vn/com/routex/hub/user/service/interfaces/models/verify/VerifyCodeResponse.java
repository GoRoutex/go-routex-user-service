package vn.com.routex.hub.user.service.interfaces.models.verify;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class VerifyCodeResponse extends BaseResponse<VerifyCodeResponse.VerifyCodeResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    public static class VerifyCodeResponseData {
        private String userId;
        private String otpCode;
        private String status;
    }
}
