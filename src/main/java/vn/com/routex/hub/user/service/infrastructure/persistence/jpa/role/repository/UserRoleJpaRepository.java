package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRoleJpaId;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRolesJpaEntity;

import java.util.List;

public interface UserRoleJpaRepository extends JpaRepository<UserRolesJpaEntity, UserRoleJpaId> {

    List<UserRolesJpaEntity> findByIdUserId(String userId);
}
