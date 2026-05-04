package vn.com.routex.hub.user.service.application.service.internal;

import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;

import java.util.List;

public interface InternalCustomerAdminService {

    Customer fetchCustomerByUserId(String userId, RequestContext context);

    List<Customer> fetchCustomersByUserIds(List<String> userIds, RequestContext context);
}
