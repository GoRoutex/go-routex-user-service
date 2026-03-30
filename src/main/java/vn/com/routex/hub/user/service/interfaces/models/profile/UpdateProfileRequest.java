package vn.com.routex.hub.user.service.interfaces.models.profile;


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
@NoArgsConstructor
@SuperBuilder
public class UpdateProfileRequest extends BaseRequest {

    @NotNull
    @NotBlank
    @Pattern(regexp = UUID_REGEX, message = UUID_MESSAGE)
    private String userId;

    @Valid
    @NotNull
    private UpdateProfileRequestData data;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateProfileRequestData {
        private String fullName;

        @Email
        private String email;
        private String phoneNumber;
        private String address;
    }
}
