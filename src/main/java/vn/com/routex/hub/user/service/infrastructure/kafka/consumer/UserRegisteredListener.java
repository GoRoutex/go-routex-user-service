package vn.com.routex.hub.user.service.infrastructure.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vn.com.routex.hub.user.service.application.service.OtpService;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.OtpMailEvent;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {

    private final OtpService otpService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("mailExecutor")
    public void handle(OtpMailEvent event) {

        try {
            sLog.info("[EVENT-CONSUMER] Consume event successfully");
            otpService.generateOtpAndSendMail(
                    event.context(),
                    event.userEvent(),
                    event.purpose()
            );
        } catch (Exception e) {
            sLog.info("[EVENT-CONSUMER] Error while processing OtpMailEvent", e);
        }
    }
}
