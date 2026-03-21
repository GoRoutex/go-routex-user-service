package vn.com.routex.hub.user.service.domain.otp.port;

import vn.com.routex.hub.user.service.domain.otp.model.Otp;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;

import java.util.Optional;

public interface OtpRepositoryPort {

    Optional<Otp> findByUserId(String userId);

    Optional<Otp> findLatestActiveOtp(String userId, OtpPurpose purpose);

    Otp save(Otp otp);
}
