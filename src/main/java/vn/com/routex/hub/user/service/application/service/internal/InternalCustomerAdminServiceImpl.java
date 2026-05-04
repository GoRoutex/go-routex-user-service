package vn.com.routex.hub.user.service.application.service.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.CUSTOMER_NOT_FOUND_MESSAGE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class InternalCustomerAdminServiceImpl implements InternalCustomerAdminService {

    private final CustomerRepositoryPort customerRepositoryPort;

    @Override
    public Customer fetchCustomerByUserId(String userId, RequestContext context) {
        return customerRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(
                        context.requestId(),
                        context.requestDateTime(),
                        context.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, CUSTOMER_NOT_FOUND_MESSAGE)
                ));
    }

    @Override
    public List<Customer> fetchCustomersByUserIds(List<String> userIds, RequestContext context) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        Map<String, Customer> customersByUserId = new LinkedHashMap<>();
        customerRepositoryPort.findByUserIds(userIds)
                .forEach(customer -> customersByUserId.put(customer.getUserId(), customer));

        return userIds.stream()
                .map(customersByUserId::get)
                .filter(java.util.Objects::nonNull)
                .toList();
    }
}
