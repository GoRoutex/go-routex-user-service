package vn.com.routex.hub.user.service.application.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult.MyCustomerProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileResult;
import vn.com.routex.hub.user.service.application.service.UserProfileService;
import vn.com.routex.hub.user.service.application.service.authorization.UserAuthorizationService;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.user.service.domain.user.model.Gender;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.log.SystemLog;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.CUSTOMER_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PROFILE_COMPLETED_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAuthorizationService userAuthorizationService;
    private final CustomerRepositoryPort customerRepositoryPort;


    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResult getUserProfile(GetUserProfileCommand command) {

        RequestContext context = command.getContext();
        String userId = command.getUserId();
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new BusinessException(context.getRequestId(), context.getRequestDateTime(), context.getChannel(),
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

        String userId = command.getUserId();

        User user = userRepositoryPort.findById(command.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE)));

        Customer customer = customerRepositoryPort.findByUserId(command.getUserId()).orElse(null);

        List<String> authorities = new ArrayList<>(userAuthorizationService.getAuthorities(command.getUserId()));

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
                .customer(myCustomer)
                .build();

    }

    @Override
    @Transactional
    public CompleteProfileResult completeProfile(CompleteProfileCommand command) {
        User user = userRepositoryPort.findById(command.getUserId())
                .orElseThrow(() -> new BusinessException(command.getContext().getRequestId(), command.getContext().getRequestDateTime(), command.getContext().getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, RECORD_NOT_FOUND_MESSAGE)));

        Customer customer = customerRepositoryPort.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(command.getContext().getRequestId(), command.getContext().getRequestDateTime(), command.getContext().getChannel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, CUSTOMER_NOT_FOUND_MESSAGE)));

        if(user.getProfileCompleted()) {
            throw new BusinessException(command.getContext().getRequestId(), command.getContext().getRequestDateTime(), command.getContext().getChannel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, PROFILE_COMPLETED_MESSAGE));
        }

        sLog.info("User before saved: {}", user);
        sLog.info("Customer before saved: {}", customer);

        user.setNationalId(command.getNationalId());
        user.setAddress(command.getAddress());
        user.setGender(Gender.valueOf(command.getGender()));
        user.setAvatarUrl(command.getAvatarUrl());
        user.setProfileCompleted(true);
        customer.setFullName(command.getFullName());

        userRepositoryPort.save(user);
        customerRepositoryPort.save(customer);

        sLog.info("User after saved: {}", user);
        sLog.info("Customer after saved: {}", customer);

        sLog.info("[COMPLETE-PROFILE] Profile completed with userId: {}", command.getUserId());


        return CompleteProfileResult.builder()
                .userId(command.getUserId())
                .fullName(command.getFullName())
                .address(command.getAddress())
                .avatarUrl(command.getAvatarUrl())
                .gender(command.getGender())
                .profileCompleted(true)
                .build();
    }
}
