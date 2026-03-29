package vn.com.routex.hub.user.service.infrastructure.kafka.consumer;

import lombok.RequiredArgsConstructor;
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
    public void handle(OtpMailEvent event) {

        sLog.info("[EVENT-CONSUMER] Consume event successfully");
        otpService.generateOtpAndSendMail(
                event.context(),
                event.userEvent(),
                event.purpose()
        );
    }
}
