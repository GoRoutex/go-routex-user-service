package vn.com.routex.hub.user.service.application.service.authorization;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.dto.authentication.ChangePasswordCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.ChangePasswordResult;
import vn.com.routex.hub.user.service.application.dto.authentication.ForgotPasswordCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.ForgotPasswordResult;
import vn.com.routex.hub.user.service.application.dto.authentication.LoginCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.LoginResult;
import vn.com.routex.hub.user.service.application.dto.authentication.LogoutCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RefreshTokenCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RefreshTokenResult;
import vn.com.routex.hub.user.service.application.dto.authentication.RegistrationCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RegistrationResult;
import vn.com.routex.hub.user.service.application.dto.authentication.VerifyOtpCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.VerifyOtpResult;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.verification.OtpGenerationResult;
import vn.com.routex.hub.user.service.application.dto.verification.ResendVerificationCommand;
import vn.com.routex.hub.user.service.application.dto.verification.ResendVerificationResult;
import vn.com.routex.hub.user.service.application.service.AuthenticationService;
import vn.com.routex.hub.user.service.application.service.OtpService;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.customer.model.CustomerMembership;
import vn.com.routex.hub.user.service.domain.customer.model.CustomerMembershipStatus;
import vn.com.routex.hub.user.service.domain.customer.model.CustomerStatus;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerMembershipRepositoryPort;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.user.service.domain.membership.MembershipTierGroup;
import vn.com.routex.hub.user.service.domain.otp.model.Otp;
import vn.com.routex.hub.user.service.domain.otp.model.OtpPurpose;
import vn.com.routex.hub.user.service.domain.otp.model.OtpStatus;
import vn.com.routex.hub.user.service.domain.otp.port.OtpRepositoryPort;
import vn.com.routex.hub.user.service.domain.role.model.Roles;
import vn.com.routex.hub.user.service.domain.role.model.RolesList;
import vn.com.routex.hub.user.service.domain.role.model.UserRoleId;
import vn.com.routex.hub.user.service.domain.role.model.UserRoles;
import vn.com.routex.hub.user.service.domain.role.port.RoleRepositoryPort;
import vn.com.routex.hub.user.service.domain.role.port.UserRoleRepositoryPort;
import vn.com.routex.hub.user.service.domain.token.model.RefreshToken;
import vn.com.routex.hub.user.service.domain.token.model.RefreshTokenStatus;
import vn.com.routex.hub.user.service.domain.token.port.RefreshTokenRepositoryPort;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.OtpMailEvent;
import vn.com.routex.hub.user.service.infrastructure.kafka.event.UserEvent;
import vn.com.routex.hub.user.service.infrastructure.persistence.constant.BusinessConstant;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.jwt.JwtService;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.BusinessConstant.MAX_ATTEMPTS_OTP;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.BusinessConstant.RESEND_COOLDOWN_SECONDS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.AUTHORIZATION_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.CONFIRM_PASSWORD_NOT_MATCHED;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_NEW_PASSWORD;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_OTP_CODE_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PASSWORD;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_REFRESH_TOKEN_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_USERNAME_EMAIL_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_USERNAME_OR_PASSWORD_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OTP_COOL_DOWN;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OTP_EXPIRED;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OTP_FAIL_ATTEMPTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OTP_NOT_ACTIVE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PHONE_NUMBER_EXISTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.REFRESH_TOKEN_EXPIRED_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.REFRESH_TOKEN_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.ROLE_NOT_FOUND_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_EXISTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_ACTIVE_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final JwtService jwtService;
    private final OtpService otpService;
    private final UserRepositoryPort userRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final OtpRepositoryPort otpRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRoleRepositoryPort userRoleRepositoryPort;
    private final UserAuthorizationService userAuthorizationService;
    private final CustomerMembershipRepositoryPort customerMembershipRepositoryPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public RegistrationResult registerUser(RegistrationCommand command) {
        sLog.info("[REGISTER] Request: {}", command);
        RequestContext context = command.getContext();

        Optional<User> optUser = userRepositoryPort.findByEmail(command.getEmail());
        if (optUser.isPresent()) {
            User existingUser = optUser.get();
            if (UserStatus.VERIFYING.equals(existingUser.getStatus())) {
                applicationEventPublisher.publishEvent(
                        OtpMailEvent.builder()
                                .context(context)
                                .userEvent(UserEvent.builder()
                                        .userId(existingUser.getId())
                                        .email(existingUser.getEmail())
                                        .phoneNumber(existingUser.getPhoneNumber())
                                        .build())
                                .purpose(OtpPurpose.REGISTER_VERIFY)
                                .build()
                );
            }
            throw BusinessException(context, DUPLICATE_ERROR, USER_EXISTS);
        }

        if (userRepositoryPort.existsByPhoneNumber(command.getPhoneNumber())) {
            throw BusinessException(context, DUPLICATE_ERROR, PHONE_NUMBER_EXISTS);
        }

        String userId = UUID.randomUUID().toString();
        User registeredUser = User.builder()
                .id(userId)
                .passwordHash(passwordEncoder.encode(command.getPassword()))
                .dob(LocalDate.parse(command.getDob()))
                .phoneNumber(command.getPhoneNumber())
                .phoneVerified(Boolean.FALSE)
                .email(command.getEmail())
                .emailVerified(Boolean.FALSE)
                .status(UserStatus.VERIFYING)
                .createdAt(OffsetDateTime.now())
                .language(command.getLanguage())
                .build();

        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .status(CustomerStatus.ACTIVE)
                .build();

        CustomerMembership customerMembership = CustomerMembership.builder()
                .id(UUID.randomUUID().toString())
                .customerId(customer.getId())
                .membershipTierId(MembershipTierGroup.BRONZE.getId())
                .currentAvailablePoints(BigDecimal.ZERO)
                .totalPoints(BigDecimal.ZERO)
                .promotedAt(OffsetDateTime.now())
                .status(CustomerMembershipStatus.ACTIVE)
                .build();

        userRepositoryPort.save(registeredUser);
        customerRepositoryPort.save(customer);
        customerMembershipRepositoryPort.save(customerMembership);


        Roles customerRole = roleRepositoryPort.findByCode(RolesList.CUSTOMER.name())
                .orElseThrow(() -> BusinessException(
                        context,
                        AUTHORIZATION_ERROR,
                        String.format(ROLE_NOT_FOUND_ERROR, RolesList.CUSTOMER.name())
                ));

        UserRoles userRoles = UserRoles.builder()
                .id(UserRoleId.builder()
                        .userId(registeredUser.getId())
                        .roleId(customerRole.getId())
                        .build())
                .assignedAt(OffsetDateTime.now())
                .build();
        userRoleRepositoryPort.save(userRoles);


        sLog.info("[PUBLISHING-EVENT]  Event publishing .... ");

        // Publish event for sending email
        applicationEventPublisher.publishEvent(
                OtpMailEvent.builder()
                        .context(context)
                        .userEvent(UserEvent.builder()
                                .userId(registeredUser.getId())
                                .email(registeredUser.getEmail())
                                .phoneNumber(registeredUser.getPhoneNumber())
                                .build())
                        .purpose(OtpPurpose.REGISTER_VERIFY)
                        .build()
        );

        return RegistrationResult.builder()
                .userId(registeredUser.getId())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .status(UserStatus.VERIFYING.name())
                .build();
    }

    @Override
    public VerifyOtpResult verifyOtpUser(VerifyOtpCommand command) {
        sLog.info("[VERIFICATION] Verification Request: {}", command);
        RequestContext context = command.getContext();

        Otp otp = otpRepositoryPort.findByUserId(command.getUserId())
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, RECORD_NOT_FOUND_MESSAGE));

        User user = userRepositoryPort.findById(command.getUserId())
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(command.getOtpCode(), otp.getOtpHash())) {
            otp.setAttemptCount(otp.getAttemptCount() + 1);
            otpRepositoryPort.save(otp);

            if (otp.getAttemptCount() >= MAX_ATTEMPTS_OTP) {
                otp.setStatus(OtpStatus.REVOKED);
                otpRepositoryPort.save(otp);
                applicationEventPublisher.publishEvent(
                        OtpMailEvent.builder()
                                .context(context)
                                .userEvent(UserEvent.builder()
                                        .userId(user.getId())
                                        .email(user.getEmail())
                                        .phoneNumber(user.getPhoneNumber())
                                        .build())
                                .purpose(OtpPurpose.REGISTER_VERIFY)
                                .build()
                );
                throw BusinessException(context, OTP_COOL_DOWN, OTP_FAIL_ATTEMPTS);
            }

            throw BusinessException(context, INVALID_INPUT_ERROR, INVALID_OTP_CODE_MESSAGE);
        }

        if (otp.getExpiredAt() == null || OffsetDateTime.now().isAfter(otp.getExpiredAt())) {
            throw BusinessException(context, OTP_COOL_DOWN, OTP_EXPIRED);
        }

        if (!OtpStatus.ACTIVE.equals(otp.getStatus())) {
            throw BusinessException(context, OTP_COOL_DOWN, OTP_NOT_ACTIVE);
        }

        otp.setConsumedAt(OffsetDateTime.now());
        otp.setStatus(OtpStatus.USED);
        otp.setUpdatedAt(OffsetDateTime.now());
        user.setStatus(UserStatus.ACTIVE);
        user.setUpdatedAt(OffsetDateTime.now());
        otpRepositoryPort.save(otp);
        userRepositoryPort.save(user);

        return VerifyOtpResult.builder()
                .otpCode(command.getOtpCode())
                .status(UserStatus.ACTIVE.name())
                .userId(command.getUserId())
                .build();
    }

    @Override
    public LoginResult login(LoginCommand command) {
        RequestContext context = command.getContext();
        User user = userRepositoryPort.findByEmail(command.getEmail())
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(command.getPassword(), user.getPasswordHash())) {
            throw BusinessException(context, INVALID_INPUT_ERROR, INVALID_USERNAME_OR_PASSWORD_MESSAGE);
        }

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw BusinessException(context, INVALID_INPUT_ERROR, USER_NOT_ACTIVE_MESSAGE);
        }

        Set<String> roles = userAuthorizationService.getRoles(user.getId());
        Set<String> authorities = userAuthorizationService.getAuthorities(user.getId());

        OffsetDateTime now = OffsetDateTime.now();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenRepositoryPort.save(RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .token(refreshToken)
                .status(RefreshTokenStatus.ACTIVE)
                .issuedAt(jwtService.extractIssuedAt(refreshToken))
                .expiredAt(jwtService.extractExpiration(refreshToken))
                .createdAt(now)
                .updatedAt(now)
                .build());

        user.setLastLoginAt(OffsetDateTime.now());
        userRepositoryPort.save(user);

        return LoginResult.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roles(roles)
                .authorities(authorities)
                .profileCompleted(user.getProfileCompleted())
                .build();
    }

    @Override
    @Transactional
    public ChangePasswordResult changePassword(ChangePasswordCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RequestContext context = command.getContext();

        User user = userRepositoryPort.findByEmail(username)
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(command.getOldPassword(), user.getPasswordHash())) {
            throw BusinessException(context, INVALID_INPUT_ERROR, INVALID_PASSWORD);
        }

        if (command.getNewPassword().equals(command.getOldPassword())) {
            throw BusinessException(context, INVALID_INPUT_ERROR, INVALID_NEW_PASSWORD);
        }

        if (!command.getNewPassword().equals(command.getConfirmNewPassword())) {
            throw BusinessException(context, INVALID_INPUT_ERROR, CONFIRM_PASSWORD_NOT_MATCHED);
        }

        user.setPasswordHash(passwordEncoder.encode(command.getNewPassword()));
        user.setUpdatedAt(OffsetDateTime.now());
        userRepositoryPort.save(user);

        refreshTokenRepositoryPort.updateAllByUserIdAndStatus(
                user.getId(),
                RefreshTokenStatus.ACTIVE,
                RefreshTokenStatus.REVOKED,
                OffsetDateTime.now()
        );

        return ChangePasswordResult.builder()
                .userId(user.getId())
                .changeAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public ForgotPasswordResult forgotPassword(ForgotPasswordCommand command) {
        RequestContext context = command.getContext();
        User user = userRepositoryPort.findByEmail(command.getEmail())
                .orElseThrow(() -> BusinessException(context, INVALID_INPUT_ERROR, INVALID_USERNAME_EMAIL_MESSAGE));

        applicationEventPublisher.publishEvent(
                OtpMailEvent.builder()
                        .context(context)
                        .userEvent(UserEvent.builder()
                                .userId(user.getId())
                                .email(user.getEmail())
                                .phoneNumber(user.getPhoneNumber())
                                .build())
                        .purpose(OtpPurpose.FORGOT_PASSWORD)
                        .build()
        );

        return ForgotPasswordResult.builder()
                .userId(user.getId())
                .expiresMinutes(BusinessConstant.EXPIRED_OTP_MINUTES)
                .build();
    }

    @Override
    public RefreshTokenResult refreshToken(RefreshTokenCommand command) {
        RequestContext context = command.getContext();
        String rawRefreshToken = command.getRefreshToken();
        OffsetDateTime now = OffsetDateTime.now();

        if (!jwtService.isTokenValid(rawRefreshToken)) {
            throw BusinessException(context, INVALID_INPUT_ERROR, INVALID_REFRESH_TOKEN_MESSAGE);
        }

        String tokenType = jwtService.extractTokenType(rawRefreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw BusinessException(context, INVALID_INPUT_ERROR, INVALID_REFRESH_TOKEN_MESSAGE);
        }

        RefreshToken refreshToken = refreshTokenRepositoryPort.findByToken(rawRefreshToken)
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, REFRESH_TOKEN_NOT_FOUND_MESSAGE));

        if (!RefreshTokenStatus.ACTIVE.equals(refreshToken.getStatus())) {
            throw BusinessException(context, RECORD_NOT_FOUND, REFRESH_TOKEN_NOT_FOUND_MESSAGE);
        }

        if (refreshToken.getExpiredAt() == null || now.isAfter(refreshToken.getExpiredAt())) {
            refreshToken.setStatus(RefreshTokenStatus.EXPIRED);
            refreshToken.setUpdatedAt(now);
            refreshTokenRepositoryPort.save(refreshToken);
            throw BusinessException(context, RECORD_NOT_FOUND, REFRESH_TOKEN_EXPIRED_MESSAGE);
        }

        String userId = jwtService.extractUserId(rawRefreshToken);
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE));

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw BusinessException(context, INVALID_INPUT_ERROR, USER_NOT_ACTIVE_MESSAGE);
        }

        refreshToken.setStatus(RefreshTokenStatus.USED);
        refreshToken.setUsedAt(now);
        refreshToken.setUpdatedAt(now);
        refreshTokenRepositoryPort.save(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        OffsetDateTime newRefreshExpiredAt = jwtService.extractExpiration(newRefreshToken);
        OffsetDateTime accessTokenExpiredAt = jwtService.extractExpiration(newAccessToken);

        refreshTokenRepositoryPort.save(RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .token(newRefreshToken)
                .status(RefreshTokenStatus.ACTIVE)
                .issuedAt(jwtService.extractIssuedAt(newRefreshToken))
                .expiredAt(newRefreshExpiredAt)
                .createdAt(now)
                .updatedAt(now)
                .build());

        return RefreshTokenResult.builder()
                .userId(user.getId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessTokenExpiredAt(accessTokenExpiredAt)
                .refreshTokenExpiredAt(newRefreshExpiredAt)
                .build();
    }

    @Override
    public void logout(LogoutCommand command) {
        RequestContext context = command.getContext();
        String refreshToken = command.getRefreshToken();
        OffsetDateTime now = OffsetDateTime.now();

        RefreshToken token = refreshTokenRepositoryPort.findByToken(refreshToken)
                .orElseThrow(() -> BusinessException(context, RECORD_NOT_FOUND, REFRESH_TOKEN_NOT_FOUND_MESSAGE));

        if (RefreshTokenStatus.ACTIVE.equals(token.getStatus())) {
            token.setStatus(RefreshTokenStatus.REVOKED);
            token.setRevokedAt(now);
            token.setUpdatedAt(now);
            refreshTokenRepositoryPort.save(token);
        }
    }

    @Override
    public ResendVerificationResult resendVerificationCode(ResendVerificationCommand command) {
        User user = userRepositoryPort.findByEmail(command.getEmail())
                .orElseThrow(() -> BusinessException(command.getContext(), RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE));

        OtpGenerationResult result = otpService.generateOtpAndSendMail(
                command.getContext(), UserEvent.builder().userId(user.getId()).email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber()).build(), command.getOtpPurpose());

        OffsetDateTime expiredAt = result.getExpiredAt();

        long expiresAfterSeconds = Duration.between(OffsetDateTime.now(), expiredAt).getSeconds();

        return ResendVerificationResult.builder()
                .expiresAfterSeconds(expiresAfterSeconds)
                .retryAfterSeconds(RESEND_COOLDOWN_SECONDS)
                .build();

    }

    private BusinessException BusinessException(RequestContext context, String code, String description) {
        return new BusinessException(
                context.getRequestId(),
                context.getRequestDateTime(),
                context.getChannel(),
                ExceptionUtils.buildResultResponse(code, description)
        );
    }
}
