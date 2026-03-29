package vn.com.routex.hub.user.service.infrastructure.kafka.event;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;

@Builder
public record OtpMailEvent(
        RequestContext context,
        UserEvent userEvent,
        OtpPurpose purpose) {
}


