package vn.com.routex.hub.user.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;
import vn.com.routex.hub.user.service.domain.ticket.TicketStatus;

@Builder
public record UpdateTicketCommand(
        RequestContext context,
        String merchantId,
        String ticketId,
        String customerName,
        String customerPhone,
        String customerEmail,
        TicketStatus status
) {}
