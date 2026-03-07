package vn.com.routex.hub.user.service.interfaces.models.register;


import lombok.AllArgsConstructor;
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
public class RegistrationResponse extends BaseResponse<RegistrationResponse.RegistrationResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    public static class RegistrationResponseData {
        private String userName;
        private String email;
        private String phoneNumber;
        private String userId;
        private String status;
    }
}
