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
public class CompleteProfileResponse extends BaseResponse<CompleteProfileResponse.CompleteProfileResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CompleteProfileResponseData {
        private String userId;
        private String fullName;
        private String gender;
        private String avatarUrl;
        private String address;
        private Boolean profileCompleted;
    }
}
