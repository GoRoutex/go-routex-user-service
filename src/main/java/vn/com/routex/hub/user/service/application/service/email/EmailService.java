package vn.com.routex.hub.user.service.application.service.email;


import vn.com.routex.hub.user.service.application.dto.email.EmailMessageCommand;

public interface EmailService {

    void sendEmail(EmailMessageCommand command);

}
