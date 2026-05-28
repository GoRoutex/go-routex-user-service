package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.customer.port.CustomerRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.customer.repository.CustomerEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerEntityRepository customerEntityRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Optional<Customer> findByUserId(String userId) {
        return customerEntityRepository.findByUserId(userId).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return customerEntityRepository.findById(id).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public List<Customer> findByUserIds(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        return customerEntityRepository.findByUserIdIn(userIds).stream()
                .map(customerPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Customer save(Customer customer) {
        return customerPersistenceMapper.toDomain(
                customerEntityRepository.save(customerPersistenceMapper.toEntity(customer))
        );
    }
}
