package vn.com.routex.hub.user.service.interfaces.models.ticket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateTicketResponse extends BaseResponse<List<CreateTicketResponse.CreateTicketResponseData>> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateTicketResponseData {
        private String ticketId;
        private String ticketCode;
        private String bookingSeatId;
        private TicketStatus status;
    }
}
