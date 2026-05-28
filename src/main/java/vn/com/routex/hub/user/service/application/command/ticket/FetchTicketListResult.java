package vn.com.routex.hub.user.service.application.command.ticket;

import lombok.Builder;
import vn.com.routex.hub.user.service.domain.ticket.model.Ticket;

import java.util.List;

@Builder
public record FetchTicketListResult(
        List<Ticket> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {}
