package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.customer;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.customer.entity.CustomerJpaEntity;

@Component
public class CustomerPersistenceMapper {

    public CustomerJpaEntity toJpaEntity(Customer customer) {
        return CustomerJpaEntity.builder()
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

    public Customer toDomain(CustomerJpaEntity customerJpaEntity) {
        return Customer.builder()
                .id(customerJpaEntity.getId())
                .userId(customerJpaEntity.getUserId())
                .status(customerJpaEntity.getStatus())
                .totalTrips(customerJpaEntity.getTotalTrips())
                .totalSpent(customerJpaEntity.getTotalSpent())
                .tripPoints(customerJpaEntity.getTripPoints())
                .createdAt(customerJpaEntity.getCreatedAt())
                .createdBy(customerJpaEntity.getCreatedBy())
                .updatedAt(customerJpaEntity.getUpdatedAt())
                .updatedBy(customerJpaEntity.getUpdatedBy())
                .build();
    }
}
