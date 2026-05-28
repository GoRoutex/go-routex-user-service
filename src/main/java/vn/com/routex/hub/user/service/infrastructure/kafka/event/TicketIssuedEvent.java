package vn.com.routex.hub.user.service.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketIssuedEvent {
    private String bookingId;
    private String bookingCode;
    private String customerId;
    private BigDecimal totalAmount;
    private String currency;
}
