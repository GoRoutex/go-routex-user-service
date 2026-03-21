package vn.com.routex.hub.user.service.application.dto.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageCommand {

    private String toEmail;
    private String fullName;
    private String verificationCode;
    private Long expireMinutes;
    private String userId;
}
