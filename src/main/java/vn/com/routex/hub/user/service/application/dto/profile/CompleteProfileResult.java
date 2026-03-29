package vn.com.routex.hub.user.service.application.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteProfileResult {
        private String userId;
        private String fullName;
        private String gender;
        private String avatarUrl;
        private String address;
        private Boolean profileCompleted;
}
