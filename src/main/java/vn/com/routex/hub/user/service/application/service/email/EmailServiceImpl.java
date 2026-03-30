package vn.com.routex.hub.user.service.application.service.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.dto.email.EmailMessageCommand;
import vn.com.routex.hub.user.service.application.service.EmailService;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.infrastructure.persistence.config.SendGridMailProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailTemplateService emailTemplateService;
    private final SendGridMailProperties properties;

    @Override
    public void sendEmail(EmailMessageCommand command) {

        Map<String, Object> variables = getStringObjectMap(command);

        String htmlBody = null;

        if(OtpPurpose.REGISTER_VERIFY.equals(command.purpose())) {
            htmlBody = emailTemplateService.processTemplate(
                    "email/verification-code",
                    variables
            );
        } else {
            htmlBody = emailTemplateService.processTemplate(
                    "email/forgot-password",
                    variables
            );
        }

        Email from = new Email(properties.getFromEmail(), properties.getFromName());
        Email to = new Email(command.toEmail());
        Content content = new Content("text/html", htmlBody);
        Mail mail = new Mail(from, properties.getVerifySubject(), to, content);

        SendGrid sendGrid = new SendGrid(properties.getApiKey());
        Request emailRequest = new Request();

        try {
            emailRequest.setMethod(Method.POST);
            emailRequest.setEndpoint("mail/send");
            emailRequest.setBody(mail.build());

            Response response = sendGrid.api(emailRequest);

            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new IllegalStateException(
                        "SendGrid send mail failed. Status=%s, body=%s"
                                .formatted(response.getStatusCode(), response.getBody())
                );
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to send email via SendGrid", ex);
        }
    }

    private @NonNull Map<String, Object> getStringObjectMap(EmailMessageCommand command) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", (command.fullName() == null || command.fullName().isBlank()) ? "bạn" : command.fullName());
        variables.put("otpCode", command.verificationCode());
        variables.put("expiredMinutes", command.expireMinutes());
        return variables;
    }
}
