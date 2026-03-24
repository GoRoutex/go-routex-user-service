package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.customer.entity.CustomerMembershipEntity;

public interface CustomerMembershipEntityRepository extends JpaRepository<CustomerMembershipEntity, String> {
}
