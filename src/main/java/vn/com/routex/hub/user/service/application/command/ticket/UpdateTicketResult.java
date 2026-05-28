package vn.com.routex.hub.user.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.ticket.TicketStatus;

@Builder
public record UpdateTicketResult(
        String ticketId,
        TicketStatus status
) {}
