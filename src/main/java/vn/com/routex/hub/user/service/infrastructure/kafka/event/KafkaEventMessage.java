package vn.com.routex.hub.user.service.infrastructure.kafka.event;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record KafkaEventMessage<T>(
        String requestId,
        String requestDateTime,
        String channel,
        String eventId,
        String eventName,
        String aggregateId,
        String source,
        Integer version,
        OffsetDateTime occurredAt,
        T data
) {
}
