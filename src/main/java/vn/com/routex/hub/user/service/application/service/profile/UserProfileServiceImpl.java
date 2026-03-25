package vn.com.routex.hub.user.service.application.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult.MyCustomerProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileResult;
import vn.com.routex.hub.user.service.application.service.UserProfileService;
import vn.com.routex.hub.user.service.application.service.authorization.UserAuthorizationService;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAuthorizationService userAuthorizationService;
    private final CustomerRepositoryPort customerRepositoryPort;

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
                .tripPoints(customer.getTripPoints())
                .totalTrips(customer.getTotalTrips())
                .totalSpent(customer.getTotalSpent())
                .lastTripAt(customer.getLastTripAt())
                .build() : null;

        return GetUserProfileResult.builder()
                .userId(userId)
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .fullName(user.getFullName())
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
                        .tripPoints(customer.getTripPoints())
                        .totalTrips(customer.getTotalTrips())
                        .totalSpent(customer.getTotalSpent())
                        .lastTripAt(customer.getLastTripAt())
                        .build()
                : null;

        return GetMyProfileResult
                .builder()
                .userId(userId)
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .authorities(authorities)
                .customer(myCustomer)
                .build();

    }
}
