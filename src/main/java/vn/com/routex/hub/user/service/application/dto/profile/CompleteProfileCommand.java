package vn.com.routex.hub.user.service.application.dto.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteProfileCommand {
    private RequestContext context;
    private String userId;
    private String fullName;
    private String nationalId;
    private String avatarUrl;
    private String address;
    private String gender;
}
