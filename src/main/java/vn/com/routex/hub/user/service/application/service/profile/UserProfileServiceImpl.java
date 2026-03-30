package vn.com.routex.hub.user.service.application.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.customer.CustomerMembershipView;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult.MyCustomerProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult.MyMembershipResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult.MyMembershipStats;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.UpdateProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.UpdateProfileResult;
import vn.com.routex.hub.user.service.application.query.CustomerMembershipQueryRepository;
import vn.com.routex.hub.user.service.application.service.UserProfileService;
import vn.com.routex.hub.user.service.application.service.authorization.UserAuthorizationService;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerMembershipRepositoryPort;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.user.service.domain.membership.model.MembershipTier;
import vn.com.routex.hub.user.service.domain.membership.port.MembershipTierRepositoryPort;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.CUSTOMER_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.EMAIL_EXISTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.OPERATION_NOT_ALLOWED;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PHONE_NUMBER_EXISTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PROFILE_COMPLETED_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SPRING_DATA_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_ACTIVE_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAuthorizationService userAuthorizationService;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerMembershipQueryRepository customerMembershipQueryRepository;
    private final MembershipTierRepositoryPort membershipTierRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResult getUserProfile(GetUserProfileCommand command) {

        RequestContext context = command.context();
        String userId = command.userId();
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new BusinessException(context.requestId(), context.requestDateTime(), context.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE)));

        Customer customer = customerRepositoryPort.findByUserId(user.getId())
                .orElse(null);

        List<String> authorities = new ArrayList<>(userAuthorizationService.getAuthorities(user.getId()));

        GetUserProfileResult.CustomerProfileResult customerProfile = customer != null ? GetUserProfileResult.CustomerProfileResult.builder()
                .customerId(customer.getId())
                .totalTrips(customer.getTotalTrips())
                .totalSpent(customer.getTotalSpent())
                .lastTripAt(customer.getLastTripAt())
                .build() : null;

        return GetUserProfileResult.builder()
                .userId(userId)
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .authorities(authorities)
                .customer(customer != null ? customerProfile : null)
                .build();

    }

    @Override
    @Transactional(readOnly = true)
    public GetMyProfileResult getMyProfile(GetMyProfileCommand command) {

        String userId = command.userId();

        User user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE)));

        Customer customer = customerRepositoryPort.findByUserId(command.userId()).orElse(null);

        CustomerMembershipView customerMemberShipView = customerMembershipQueryRepository.findMembershipSummaryByUserId(command.userId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, SPRING_DATA_NOT_FOUND_MESSAGE)));

        sLog.info("Member ship view Priority Level: {}", customerMemberShipView.priorityLevel());

        Optional<MembershipTier> nextTierOpt = membershipTierRepositoryPort.findByPriorityLevel(customerMemberShipView.priorityLevel() + 1);

        BigDecimal pointToNextTier = nextTierOpt
                .map(next -> next.getMinPoints().subtract(customerMemberShipView.tripPoints()).max(BigDecimal.ZERO))
                .orElse(BigDecimal.ZERO);

        List<String> authorities = new ArrayList<>(userAuthorizationService.getAuthorities(command.userId()));

        MyMembershipResult myMembership = MyMembershipResult.builder()
                .currentPoint(customerMemberShipView.tripPoints())
                .discountPercent(customerMemberShipView.discountPercent())
                .priorityLevel(customerMemberShipView.priorityLevel())
                .build();

        MyMembershipStats myMembershipStats = customer != null ? MyMembershipStats.builder()
                .totalTrips(customer.getTotalTrips())
                .badge(customerMemberShipView.currentBadge())
                .totalSpent(customer.getTotalSpent())
                .pointToNextTier(pointToNextTier)
                .pointMultiplier(customerMemberShipView.pointMultiplier())
                .nextTierName(nextTierOpt.map(MembershipTier::getBadge).orElse(null))
                .build() : null;

        MyCustomerProfileResult myCustomer = customer != null ?
                MyCustomerProfileResult
                        .builder()
                        .customerId(customer.getId())
                        .fullName(customer.getFullName())
                        .tripPoints(customer.getTripPoints())
                        .totalTrips(customer.getTotalTrips())
                        .totalSpent(customer.getTotalSpent())
                        .lastTripAt(customer.getLastTripAt())
                        .lastBookingAt(customer.getLastBookingAt())
                        .build()
                : null;

        return GetMyProfileResult
                .builder()
                .userId(userId)
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .status(user.getStatus())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .nationalId(user.getNationalId())
                .address(user.getAddress())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .authorities(authorities)
                .membership(myMembership)
                .stats(myMembershipStats)
                .customer(myCustomer)
                .build();

    }

    @Override
    @Transactional
    public CompleteProfileResult completeProfile(CompleteProfileCommand command) {
        User user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, RECORD_NOT_FOUND_MESSAGE)));

        Customer customer = customerRepositoryPort.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, CUSTOMER_NOT_FOUND_MESSAGE)));

        if(user.getProfileCompleted()) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, PROFILE_COMPLETED_MESSAGE));
        }

        sLog.info("User before saved: {}", user);
        sLog.info("Customer before saved: {}", customer);

        user.setNationalId(command.nationalId());
        user.setAddress(command.address());
        user.setGender(Gender.valueOf(command.gender()));
        user.setAvatarUrl(command.avatarUrl());
        user.setProfileCompleted(true);
        customer.setFullName(command.fullName());

        userRepositoryPort.save(user);
        customerRepositoryPort.save(customer);

        sLog.info("User after saved: {}", user);
        sLog.info("Customer after saved: {}", customer);

        sLog.info("[COMPLETE-PROFILE] Profile completed with userId: {}", command.userId());


        return CompleteProfileResult.builder()
                .userId(command.userId())
                .fullName(command.fullName())
                .address(command.address())
                .avatarUrl(command.avatarUrl())
                .gender(command.gender())
                .profileCompleted(true)
                .build();
    }

    @Override
    @Transactional
    public UpdateProfileResult updateProfile(UpdateProfileCommand command) {
        User user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE)));


        Customer customer = customerRepositoryPort.findByUserId(command.userId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, CUSTOMER_NOT_FOUND_MESSAGE)));

        if(!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(OPERATION_NOT_ALLOWED, USER_NOT_ACTIVE_MESSAGE));
        }

        if (command.email() != null && !command.email().equals(user.getEmail())) {
            boolean emailExists = userRepositoryPort.existsByEmail(command.email());
            if (emailExists) {
                throw new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, EMAIL_EXISTS)
                );
            }
        }

        if(command.phoneNumber() != null && !command.phoneNumber().equals(user.getPhoneNumber())) {
            boolean phoneNumberExists = userRepositoryPort.existsByPhoneNumber(command.phoneNumber());
            if(phoneNumberExists) {
                throw new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, PHONE_NUMBER_EXISTS)
                );
            }
        }

        Optional.ofNullable(command.fullName())
                .ifPresent(customer::setFullName);

        Optional.ofNullable(command.address())
                .ifPresent(user::setAddress);

        Optional.ofNullable(command.email())
                .ifPresent(user::setEmail);

        Optional.ofNullable(command.phoneNumber())
                .ifPresent(user::setPhoneNumber);

        customerRepositoryPort.save(customer);
        userRepositoryPort.save(user);

        return UpdateProfileResult.builder()
                .fullName(command.fullName())
                .phoneNumber(command.phoneNumber())
                .address(command.address())
                .email(command.email())
                .build();
    }
}
