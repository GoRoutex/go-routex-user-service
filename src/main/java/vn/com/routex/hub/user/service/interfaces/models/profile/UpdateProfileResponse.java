package vn.com.routex.hub.user.service.interfaces.models.profile;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateProfileResponse extends BaseResponse<UpdateProfileResponse.UpdateProfileResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class UpdateProfileResponseData {
        private String userId;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String address;
        private String avatarUrl;
    }
}
