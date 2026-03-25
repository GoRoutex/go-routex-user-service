package vn.com.routex.hub.user.service.interfaces.models.verify;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResendVerificationResponse extends BaseResponse<ResendVerificationResponse.ResendVerificationResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ResendVerificationResponseData {
        private long retryAfterSeconds;
    }

}
