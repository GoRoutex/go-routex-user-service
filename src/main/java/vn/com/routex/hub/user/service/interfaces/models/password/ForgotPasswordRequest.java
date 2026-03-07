package vn.com.routex.hub.user.service.interfaces.models.password;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.UUID_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.UUID_REGEX;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class ForgotPasswordRequest extends BaseRequest {

    @Valid
    @NotNull
    private ForgotPasswordRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ForgotPasswordRequestData {

        @NotNull
        @NotBlank
        private String username;

        @Email
        @NotNull
        @NotBlank
        private String email;
    }
}
