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
public class UploadAvatarResponse extends BaseResponse<UploadAvatarResponse.UploadAvatarResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class UploadAvatarResponseData {
        private String userId;
        private String avatarUrl;
    }
}

