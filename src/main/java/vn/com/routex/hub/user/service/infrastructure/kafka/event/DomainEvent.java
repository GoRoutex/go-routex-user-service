package vn.com.routex.hub.user.service.infrastructure.kafka.event;

import java.util.Map;

public record DomainEvent(
        String eventId,
        String eventType,
        String aggregateId,
        Map<String, Object> header,
        Map<String, Object> payload,
        String timestamp
) {
}
