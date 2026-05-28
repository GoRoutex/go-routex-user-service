package vn.com.routex.hub.user.service.application.service;


import vn.com.routex.hub.user.service.application.command.ticket.FetchCustomerTicketsQuery;
import vn.com.routex.hub.user.service.application.command.ticket.FetchTicketDetailQuery;
import vn.com.routex.hub.user.service.application.command.ticket.FetchTicketDetailResult;
import vn.com.routex.hub.user.service.application.command.ticket.FetchTicketListResult;

public interface TicketService {
    FetchTicketListResult getCustomerTickets(FetchCustomerTicketsQuery query);
    FetchTicketDetailResult getCustomerTicketDetail(FetchTicketDetailQuery query);
}

