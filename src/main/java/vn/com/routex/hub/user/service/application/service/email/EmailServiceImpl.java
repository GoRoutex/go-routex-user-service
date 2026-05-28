package vn.com.routex.hub.user.service.application.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.command.email.EmailMessageCommand;
import vn.com.routex.hub.user.service.application.service.EmailService;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.EmailNotificationEvent;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.KafkaEventMessage;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.infrastructure.utils.JsonUtils;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Value("${spring.kafka.events.notification-email}")
    private String emailEventName;

    @Override
    public void sendEmail(EmailMessageCommand command) {
        sLog.info("[EMAIL-PUBLISHER] Preparing asynchronous email event for toEmail={}, purpose={}", command.toEmail(), command.purpose());

        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", (command.fullName() == null || command.fullName().isBlank()) ? "bạn" : command.fullName());
        variables.put("otpCode", command.verificationCode());
        variables.put("expiredMinutes", command.expireMinutes());

        String templateName;
        String subject;
        if (OtpPurpose.REGISTER_VERIFY.equals(command.purpose())) {
            templateName = "email/verification-code";
            subject = "Go Routex - Xác thực tài khoản";
        } else {
            templateName = "email/forgot-password";
            subject = "Go Routex - Đặt lại mật khẩu";
        }

        EmailNotificationEvent emailEvent = EmailNotificationEvent.builder()
                .toEmail(command.toEmail())
                .subject(subject)
                .templateName(templateName)
                .variables(variables)
                .build();

        KafkaEventMessage<EmailNotificationEvent> message = KafkaEventMessage.<EmailNotificationEvent>builder()
                .eventId(UUID.randomUUID().toString())
                .eventName(emailEventName)
                .aggregateId(command.toEmail())
                .occurredAt(OffsetDateTime.now())
                .data(emailEvent)
                .build();

        try {
            String payload = JsonUtils.parseToJsonStr(message);
            sLog.info("[EMAIL-PUBLISHER] Publishing event payload to topic routex.notification.email");
            kafkaTemplate.send(emailEventName, command.toEmail(), payload);
            sLog.info("[EMAIL-PUBLISHER] Successfully published email notification event to Kafka");
        } catch (Exception e) {
            sLog.error("[EMAIL-PUBLISHER] Failed to publish email event to Kafka", e);
            throw new IllegalStateException("Failed to publish email notification event", e);
        }
    }
}
