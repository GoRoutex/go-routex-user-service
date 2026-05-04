package vn.com.routex.hub.user.service.domain.customer.port;

import vn.com.routex.hub.user.service.domain.customer.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {

    Optional<Customer> findByUserId(String userId);
    List<Customer> findByUserIds(List<String> userIds);
    Customer save(Customer customer);
}
