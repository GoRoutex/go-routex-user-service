package vn.com.routex.hub.user.service.application.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.command.ticket.FetchCustomerTicketsQuery;
import vn.com.routex.hub.user.service.application.command.ticket.FetchTicketDetailQuery;
import vn.com.routex.hub.user.service.application.command.ticket.FetchTicketDetailResult;
import vn.com.routex.hub.user.service.application.command.ticket.FetchTicketListResult;
import vn.com.routex.hub.user.service.application.service.TicketService;
import vn.com.routex.hub.user.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.user.service.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepositoryPort ticketRepositoryPort;

    @Override
    public FetchTicketListResult getCustomerTickets(FetchCustomerTicketsQuery query) {
        Page<Ticket> page = ticketRepositoryPort.findByCustomer(
                query.customerEmail(),
                query.customerPhone(),
                query.ticketCode(),
                query.fromDate(),
                query.toDate(),
                PageRequest.of(query.pageNumber() - 1, query.pageSize())
        );

        return FetchTicketListResult.builder()
                .items(page.getContent())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public FetchTicketDetailResult getCustomerTicketDetail(FetchTicketDetailQuery query) {
        Ticket ticket = ticketRepositoryPort.findById(query.ticketId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Ticket not found")));

        // Security Check: Verify ownership by email or phone
        // In a real app, we get this from the JWT (RequestContext)
        String identityEmail = query.context().userEmail();
        String identityPhone = query.context().userPhone();

        boolean isOwner = (identityEmail != null && identityEmail.equalsIgnoreCase(ticket.getCustomerEmail())) ||
                          (identityPhone != null && identityPhone.equals(ticket.getCustomerPhone()));

        if (!isOwner) {
            throw new BusinessException(ExceptionUtils.buildResultResponse("ACCESS_DENIED", "You do not have permission to view this ticket"));
        }

        return FetchTicketDetailResult.builder()
                .ticket(ticket)
                .build();
    }
}
