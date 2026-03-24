package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.customer;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.customer.entity.CustomerEntity;

@Component
public class CustomerPersistenceMapper {

    public CustomerEntity toJpaEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .userId(customer.getUserId())
                .status(customer.getStatus())
                .totalTrips(customer.getTotalTrips())
                .totalSpent(customer.getTotalSpent())
                .tripPoints(customer.getTripPoints())
                .createdAt(customer.getCreatedAt())
                .createdBy(customer.getCreatedBy())
                .updatedAt(customer.getUpdatedAt())
                .updatedBy(customer.getUpdatedBy())
                .build();
    }

    public Customer toDomain(CustomerEntity customerEntity) {
        return Customer.builder()
                .id(customerEntity.getId())
                .userId(customerEntity.getUserId())
                .status(customerEntity.getStatus())
                .totalTrips(customerEntity.getTotalTrips())
                .totalSpent(customerEntity.getTotalSpent())
                .tripPoints(customerEntity.getTripPoints())
                .createdAt(customerEntity.getCreatedAt())
                .createdBy(customerEntity.getCreatedBy())
                .updatedAt(customerEntity.getUpdatedAt())
                .updatedBy(customerEntity.getUpdatedBy())
                .build();
    }
}
