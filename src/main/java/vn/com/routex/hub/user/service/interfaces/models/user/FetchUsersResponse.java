package vn.com.routex.hub.user.service.interfaces.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchUsersResponse extends BaseResponse<FetchUsersResponse.FetchUsersResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchUsersResponsePage {
        private List<FetchUserResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchUserResponseData {
        private String id;
        private String email;
        private String fullName;
        private String phoneNumber;
        private String avatarUrl;
        private Set<String> roles;
        private LocalDate dob;
        private Gender gender;
        private Boolean phoneVerified;
        private Boolean profileCompleted;
        private Boolean emailVerified;
        private UserStatus status;
        private String language;
        private String timezone;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
