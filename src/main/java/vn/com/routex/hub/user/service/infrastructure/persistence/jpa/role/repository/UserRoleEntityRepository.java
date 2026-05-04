package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRoleEntityId;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRolesEntity;

import java.util.List;

public interface UserRoleEntityRepository extends JpaRepository<UserRolesEntity, UserRoleEntityId> {

    List<UserRolesEntity> findByIdUserId(String userId);
}
