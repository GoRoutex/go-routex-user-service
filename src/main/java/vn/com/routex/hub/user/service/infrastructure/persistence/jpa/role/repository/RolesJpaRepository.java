package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.RolesJpaEntity;

import java.util.Optional;

public interface RolesJpaRepository extends JpaRepository<RolesJpaEntity, String> {

    Optional<RolesJpaEntity> findByCode(String code);
}
