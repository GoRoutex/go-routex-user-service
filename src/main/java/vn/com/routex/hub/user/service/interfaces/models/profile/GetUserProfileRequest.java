package vn.com.routex.hub.user.service.interfaces.models.profile;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetUserProfileRequest extends BaseRequest {


    @Valid
    @NotNull
    private GetUserProfileRequestData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class GetUserProfileRequestData {
        private String userId;
    }
}
