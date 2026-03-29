package vn.com.routex.hub.user.service.interfaces.models.verify;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.UUID_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.UUID_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VerifyCodeRequest extends BaseRequest {


    @Valid
    @NotNull
    private VerifyCodeRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class VerifyCodeRequestData {

        @NotNull
        @NotBlank
        @Size(max = 36)
        @Pattern(regexp = UUID_REGEX, message = UUID_MESSAGE)
        private String userId;
        @NotNull
        @NotBlank
        private String otpCode;
    }
}
