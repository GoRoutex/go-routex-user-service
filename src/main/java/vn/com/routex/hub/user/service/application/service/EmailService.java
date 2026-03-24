package vn.com.routex.hub.user.service.application.service;


import vn.com.routex.hub.user.service.application.dto.email.EmailMessageCommand;

public interface EmailService {

    void sendEmail(EmailMessageCommand command);

}
