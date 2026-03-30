package vn.com.routex.hub.user.service.interfaces.models.password;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResetPasswordRequest extends BaseRequest {

    @Valid
    @NotNull
    private ResetPasswordRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ResetPasswordRequestData {
        private String otpCode;
        private String userId;
        private String newPassword;
        private String confirmPassword;
    }
}
