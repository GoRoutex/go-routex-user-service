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

        String htmlBody = emailTemplateService.processTemplate(
                "email/verification-code",
                variables
        );

        Email from = new Email(properties.getFromEmail(), properties.getFromName());
        Email to = new Email(command.getToEmail());
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
        String verifyLink =
                properties.getBaseUrl() + "/api/v1/user-service/authentication/verify/" + command.getUserId();

        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", (command.getFullName() == null || command.getFullName().isBlank()) ? "bạn" : command.getFullName());
        variables.put("otpCode", command.getVerificationCode());
        variables.put("expiredMinutes", command.getExpireMinutes());
        variables.put("verifyLink", verifyLink);
        return variables;
    }
}
