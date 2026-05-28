package vn.com.routex.hub.user.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.ticket.TicketStatus;

@Builder
public record CreateTicketResult(
        String ticketId,
        String ticketCode,
        String bookingSeatId,
        TicketStatus status
) {}
