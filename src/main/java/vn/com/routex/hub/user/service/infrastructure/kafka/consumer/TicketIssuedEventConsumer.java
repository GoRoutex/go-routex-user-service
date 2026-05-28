package vn.com.routex.hub.user.service.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.application.service.internal.InternalCustomerAdminService;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.DomainEvent;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.TicketIssuedEvent;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.infrastructure.utils.JsonUtils;

@Component
@RequiredArgsConstructor
public class TicketIssuedEventConsumer {

    @Value("${spring.kafka.events.ticket-issued}")
    private String ticketIssuedEvent;

    private final InternalCustomerAdminService internalCustomerAdminService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final ObjectMapper objectMapper = JsonUtils.getObjectMapper();

    @KafkaListener(
            topics = "${spring.kafka.topics.booking}",
            groupId = "${spring.kafka.group-id.membership}"
    )
    public void ticketIssuedConsumer(String payload, Acknowledgment acknowledgment) {
        sLog.info("[MEMBERSHIP-EVENT] Raw Payload: {}", payload);
        
        DomainEvent event;
        try {
            event = objectMapper.readValue(payload, new TypeReference<>() {});
        } catch (Exception ex) {
            sLog.info("[MEMBERSHIP-EVENT] Failed to parse event payload, dropping poison pill. Error: {}", ex.getMessage());
            acknowledgment.acknowledge();
            return;
        }

        if (event == null || event.payload() == null || event.payload().get("data") == null) {
            sLog.info("[MEMBERSHIP-EVENT] Invalid event format, missing data payload");
            acknowledgment.acknowledge();
            return;
        }

        if (!ticketIssuedEvent.equals(event.eventType())) {
            acknowledgment.acknowledge();
            return;
        }

        TicketIssuedEvent ticketEvent;
        try {
            ticketEvent = objectMapper.convertValue(event.payload().get("data"), TicketIssuedEvent.class);
        } catch (Exception ex) {
            sLog.info("[MEMBERSHIP-EVENT] Failed to convert event data, dropping poison pill. Error: {}", ex.getMessage());
            acknowledgment.acknowledge();
            return;
        }

        try {
            sLog.info("[MEMBERSHIP-EVENT] Processing ticket issued event for customerId={}", ticketEvent.getCustomerId());

            if (ticketEvent.getCustomerId() != null && !ticketEvent.getCustomerId().isBlank()) {
                // Add points based on totalAmount
                internalCustomerAdminService.addMembershipPoints(ticketEvent.getCustomerId(), ticketEvent.getTotalAmount());
                sLog.info("[MEMBERSHIP-EVENT] Points added for customerId={}", ticketEvent.getCustomerId());
            } else {
                sLog.info("[MEMBERSHIP-EVENT] Ignored event because customerId is null (Guest booking)");
            }

            acknowledgment.acknowledge();
        } catch (Exception ex) {
            sLog.info("[MEMBERSHIP-EVENT] Processing failed for eventId={}. Triggering retry.", event.eventId(), ex);
            // Throw exception to trigger Spring Kafka retry mechanism (DefaultErrorHandler with BackOff)
            throw new RuntimeException("Failed to process membership event, triggering retry", ex);
        }
    }
}

