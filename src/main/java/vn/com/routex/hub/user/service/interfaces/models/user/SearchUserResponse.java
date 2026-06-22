package vn.com.routex.hub.user.service.interfaces.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SearchUserResponse extends BaseResponse<List<SearchUserResponse.SearchUserResponseData>> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchUserResponseData {
        private String userId;
        private String fullName;
        private String phoneNumber;
        private String email;
        private String avatarUrl;
    }
}
