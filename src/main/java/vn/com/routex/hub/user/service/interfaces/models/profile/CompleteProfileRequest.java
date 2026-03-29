package vn.com.routex.hub.user.service.interfaces.models.profile;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApplicationConstant.GENDER_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CompleteProfileRequest extends BaseRequest {

    @Valid
    @NotNull
    private CompleteProfileRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CompleteProfileRequestData {
        private String userId;
        private String fullName;
        private String nationalId;
        private String avatarUrl;
        private String address;
        @Pattern(regexp = GENDER_REGEX, message = "must be MALE, FEMALE, LGBT or OTHER")
        private String gender;
    }
}
