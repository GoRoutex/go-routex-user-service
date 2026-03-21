package vn.com.routex.hub.user.service.domain.customer.port;

import vn.com.routex.hub.user.service.domain.customer.model.Customer;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);
}
