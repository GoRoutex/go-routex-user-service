package vn.com.routex.hub.user.service.interfaces.models.password;


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
public class ChangePasswordResponse extends BaseResponse<ChangePasswordResponse.ChangePasswordResponseData> {

    @Getter
    @Setter
    @SuperBuilder
    public static class ChangePasswordResponseData {
        private String userId;
        private OffsetDateTime changeAt;
    }
}
