package vn.com.routex.hub.user.service.interfaces.models.password;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ForgotPasswordResponse extends BaseResponse<ForgotPasswordResponse.ForgotPasswordResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    public static class ForgotPasswordResponseData {
        private String userId;
        private Long expiresMinutes;
    }
}
