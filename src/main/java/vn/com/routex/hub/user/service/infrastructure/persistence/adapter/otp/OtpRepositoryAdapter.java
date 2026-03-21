package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.otp.model.Otp;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.domain.otp.port.OtpRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.otp.repository.OtpJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OtpRepositoryAdapter implements OtpRepositoryPort {

    private final OtpJpaRepository otpJpaRepository;
    private final OtpPersistenceMapper otpPersistenceMapper;

    @Override
    public Optional<Otp> findByUserId(String userId) {
        return otpJpaRepository.findByUserId(userId).map(otpPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Otp> findLatestActiveOtp(String userId, OtpPurpose purpose) {
        return otpJpaRepository.findLatestActiveOtp(userId, purpose).map(otpPersistenceMapper::toDomain);
    }

    @Override
    public Otp save(Otp otp) {
        return otpPersistenceMapper.toDomain(otpJpaRepository.save(otpPersistenceMapper.toJpaEntity(otp)));
    }
}
