package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.history.entity.MembershipHistoryEntity;

public interface MembershipHistoryEntityRepository extends JpaRepository<MembershipHistoryEntity, String> {
}
