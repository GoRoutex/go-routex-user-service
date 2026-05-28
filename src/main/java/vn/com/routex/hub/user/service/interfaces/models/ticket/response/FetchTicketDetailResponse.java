package vn.com.routex.hub.user.service.interfaces.models.ticket.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchTicketDetailResponse extends BaseResponse<TicketResponse> {
}
