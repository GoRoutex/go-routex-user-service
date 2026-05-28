package vn.com.routex.hub.user.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.ticket.model.Ticket;

@Builder
public record FetchTicketDetailResult(
        Ticket ticket
) {}
