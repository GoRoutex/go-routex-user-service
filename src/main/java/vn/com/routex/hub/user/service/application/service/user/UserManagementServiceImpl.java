package vn.com.routex.hub.user.service.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;
import vn.com.routex.hub.user.service.application.command.user.DeleteUserCommand;
import vn.com.routex.hub.user.service.application.command.user.DeleteUserResult;
import vn.com.routex.hub.user.service.application.command.user.FetchUserDetailQuery;
import vn.com.routex.hub.user.service.application.command.user.FetchUserDetailResult;
import vn.com.routex.hub.user.service.application.command.user.FetchUsersQuery;
import vn.com.routex.hub.user.service.application.command.user.FetchUsersResult;
import vn.com.routex.hub.user.service.application.command.user.UpdateUserCommand;
import vn.com.routex.hub.user.service.application.command.user.UpdateUserResult;
import vn.com.routex.hub.user.service.application.service.UserManagementService;
import vn.com.routex.hub.user.service.application.service.internal.InternalCustomerAdminService;
import vn.com.routex.hub.user.service.domain.common.PagedResult;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.model.UserStatus;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.CUSTOMER_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.EMAIL_EXISTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.PHONE_NUMBER_EXISTS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int MAX_PAGE_SIZE = 100;

    private final UserRepositoryPort userRepositoryPort;
    private final InternalCustomerAdminService internalCustomerAdminService;

    @Override
    public FetchUsersResult fetchUsers(FetchUsersQuery query) {
        int pageSize = parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize", query.context());
        int pageNumber = parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber", query.context());

        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw businessException(query.context(), INVALID_INPUT_ERROR, "pageSize must be between 1 and 100");
        }
        if (pageNumber < 1) {
            throw businessException(query.context(), INVALID_INPUT_ERROR, "pageNumber must be greater than 0");
        }

        PagedResult<User> page = userRepositoryPort.fetch(pageNumber - 1, pageSize);
        Map<String, Customer> customersByUserId = fetchCustomersByUserIds(
                page.getItems().stream().map(User::getId).toList(),
                query.context()
        );

        List<FetchUsersResult.FetchUserItemResult> items = page.getItems().stream()
                .map(user -> {
                    Customer customer = customersByUserId.get(user.getId());
                    if (customer == null) {
                        throw businessException(query.context(), RECORD_NOT_FOUND, CUSTOMER_NOT_FOUND_MESSAGE);
                    }
                    return toFetchUserItem(user, customer);
                })
                .toList();

        return FetchUsersResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public FetchUserDetailResult fetchUserDetail(FetchUserDetailQuery query) {
        return toFetchUserDetailResult(loadUser(query.userId(), query.context()));
    }

    @Override
    public UpdateUserResult updateUser(UpdateUserCommand command) {
        User existing = loadUser(command.userId(), command.context());
        validateDuplicates(command, existing);

        existing.setEmail(firstNonBlank(command.email(), existing.getEmail()));
        existing.setPhoneNumber(firstNonBlank(command.phoneNumber(), existing.getPhoneNumber()));
        existing.setAvatarUrl(firstNonBlank(command.avatarUrl(), existing.getAvatarUrl()));
        existing.setAddress(firstNonBlank(command.address(), existing.getAddress()));
        existing.setDob(command.dob() == null ? existing.getDob() : command.dob());
        existing.setGender(command.gender() == null ? existing.getGender() : command.gender());
        existing.setNationalId(firstNonBlank(command.nationalId(), existing.getNationalId()));
        existing.setPhoneVerified(command.phoneVerified() == null ? existing.getPhoneVerified() : command.phoneVerified());
        existing.setProfileCompleted(command.profileCompleted() == null ? existing.getProfileCompleted() : command.profileCompleted());
        existing.setEmailVerified(command.emailVerified() == null ? existing.getEmailVerified() : command.emailVerified());
        existing.setStatus(command.status() == null ? existing.getStatus() : command.status());
        existing.setLanguage(firstNonBlank(command.language(), existing.getLanguage()));
        existing.setTimezone(firstNonBlank(command.timezone(), existing.getTimezone()));
        existing.setFailLoginCount(command.failLoginCount() == null ? existing.getFailLoginCount() : command.failLoginCount());
        existing.setLastLoginAt(command.lastLoginAt() == null ? existing.getLastLoginAt() : command.lastLoginAt());
        existing.setLockedUntil(command.lockedUntil() == null ? existing.getLockedUntil() : command.lockedUntil());
        existing.setUpdatedBy(firstNonBlank(command.updatedBy(), existing.getUpdatedBy()));

        User saved = userRepositoryPort.save(existing);

        return toUpdateUserResult(saved);
    }

    @Override
    public DeleteUserResult deleteUser(DeleteUserCommand command) {
        User existing = loadUser(command.userId(), command.context());
        existing.setStatus(UserStatus.INACTIVE);
        existing.setUpdatedBy(firstNonBlank(command.updatedBy(), existing.getUpdatedBy()));

        User saved = userRepositoryPort.save(existing);

        return DeleteUserResult.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .build();
    }

    private Map<String, Customer> fetchCustomersByUserIds(List<String> userIds, RequestContext context) {
        return internalCustomerAdminService.fetchCustomersByUserIds(userIds, context).stream()
                .collect(Collectors.toMap(Customer::getUserId, Function.identity(), (left, right) -> left));
    }

    private void validateDuplicates(UpdateUserCommand command, User existing) {
        if (command.email() != null && !command.email().isBlank()
                && userRepositoryPort.existsByEmailAndIdNot(command.email().trim(), existing.getId())) {
            throw businessException(command.context(), DUPLICATE_ERROR, EMAIL_EXISTS);
        }

        if (command.phoneNumber() != null && !command.phoneNumber().isBlank()
                && userRepositoryPort.existsByPhoneNumberAndIdNot(command.phoneNumber().trim(), existing.getId())) {
            throw businessException(command.context(), DUPLICATE_ERROR, PHONE_NUMBER_EXISTS);
        }
    }

    private User loadUser(String userId, RequestContext context) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> businessException(context, RECORD_NOT_FOUND, USER_NOT_FOUND_MESSAGE));
    }

    private FetchUsersResult.FetchUserItemResult toFetchUserItem(User user, Customer customer) {
        return FetchUsersResult.FetchUserItemResult.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(customer.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .dob(user.getDob())
                .gender(user.getGender())
                .phoneVerified(user.getPhoneVerified())
                .profileCompleted(user.getProfileCompleted())
                .emailVerified(user.getEmailVerified())
                .status(user.getStatus())
                .language(user.getLanguage())
                .timezone(user.getTimezone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private FetchUserDetailResult toFetchUserDetailResult(User user) {
        return FetchUserDetailResult.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .dob(user.getDob())
                .gender(user.getGender())
                .nationalId(user.getNationalId())
                .phoneVerified(user.getPhoneVerified())
                .profileCompleted(user.getProfileCompleted())
                .emailVerified(user.getEmailVerified())
                .status(user.getStatus())
                .language(user.getLanguage())
                .timezone(user.getTimezone())
                .failLoginCount(user.getFailLoginCount())
                .lastLoginAt(user.getLastLoginAt())
                .lockedUntil(user.getLockedUntil())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

    private UpdateUserResult toUpdateUserResult(User user) {
        return UpdateUserResult.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .dob(user.getDob())
                .gender(user.getGender())
                .nationalId(user.getNationalId())
                .phoneVerified(user.getPhoneVerified())
                .profileCompleted(user.getProfileCompleted())
                .emailVerified(user.getEmailVerified())
                .status(user.getStatus())
                .language(user.getLanguage())
                .timezone(user.getTimezone())
                .failLoginCount(user.getFailLoginCount())
                .lastLoginAt(user.getLastLoginAt())
                .lockedUntil(user.getLockedUntil())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

    private int parseIntOrDefault(String value, int defaultValue, String fieldName, RequestContext context) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw businessException(context, INVALID_INPUT_ERROR, fieldName + " must be a number");
        }
    }

    private String firstNonBlank(String candidate, String fallback) {
        return candidate == null || candidate.isBlank() ? fallback : candidate.trim();
    }

    private BusinessException businessException(RequestContext context, String code, String description) {
        return new BusinessException(
                context.requestId(),
                context.requestDateTime(),
                context.channel(),
                ExceptionUtils.buildResultResponse(code, description)
        );
    }
}
