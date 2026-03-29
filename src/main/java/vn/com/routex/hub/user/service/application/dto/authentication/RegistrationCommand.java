package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCommand {

    private RequestContext context;
    private String email;
    private String password;
    private String phoneNumber;
    private String dob;
    private String language;
}
