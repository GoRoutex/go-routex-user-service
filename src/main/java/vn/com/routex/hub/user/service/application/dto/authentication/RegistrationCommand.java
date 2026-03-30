package vn.com.routex.hub.user.service.application.dto.authentication;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;

@Builder
public record RegistrationCommand(
        RequestContext context,
        String email,
        String password,
        String phoneNumber,
        String dob,
        String language
) {
}

