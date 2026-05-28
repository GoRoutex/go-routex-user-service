package vn.com.routex.hub.user.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;

@Builder
public record FetchTicketDetailQuery(
        RequestContext context,
        String merchantId,
        String ticketId
) {}
