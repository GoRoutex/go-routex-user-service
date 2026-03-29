package vn.com.routex.hub.user.service.infrastructure.kafka.event;

import lombok.Builder;

@Builder
public record UserEvent (String userId, String email, String phoneNumber) {
}
