package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.role;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.role.model.Authorities;
import vn.com.routex.hub.user.service.domain.role.model.Roles;
import vn.com.routex.hub.user.service.domain.role.model.UserRoleId;
import vn.com.routex.hub.user.service.domain.role.model.UserRoles;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.AuthoritiesEntity;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.RolesEntity;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRoleEntityId;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRolesEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RolePersistenceMapper {

    public Roles toDomain(RolesEntity rolesEntity) {
        Set<Authorities> authorities = rolesEntity.getAuthorities().stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());

        return Roles.builder()
                .id(rolesEntity.getId())
                .code(rolesEntity.getCode())
                .name(rolesEntity.getName())
                .description(rolesEntity.getDescription())
                .enabled(rolesEntity.getEnabled())
                .authorities(authorities)
                .createdAt(rolesEntity.getCreatedAt())
                .createdBy(rolesEntity.getCreatedBy())
                .updatedAt(rolesEntity.getUpdatedAt())
                .updatedBy(rolesEntity.getUpdatedBy())
                .build();
    }

    public Authorities toDomain(AuthoritiesEntity authoritiesEntity) {
        return Authorities.builder()
                .id(authoritiesEntity.getId())
                .code(authoritiesEntity.getCode())
                .name(authoritiesEntity.getName())
                .description(authoritiesEntity.getDescription())
                .enabled(authoritiesEntity.getEnabled())
                .createdAt(authoritiesEntity.getCreatedAt())
                .createdBy(authoritiesEntity.getCreatedBy())
                .updatedAt(authoritiesEntity.getUpdatedAt())
                .updatedBy(authoritiesEntity.getUpdatedBy())
                .build();
    }

    public UserRoles toDomain(UserRolesEntity userRolesEntity) {
        return UserRoles.builder()
                .id(UserRoleId.builder()
                        .userId(userRolesEntity.getId().getUserId())
                        .roleId(userRolesEntity.getId().getRoleId())
                        .build())
                .assignedAt(userRolesEntity.getAssignedAt())
                .build();
    }

    public UserRolesEntity toJpaEntity(UserRoles userRoles) {
        return UserRolesEntity.builder()
                .id(UserRoleEntityId.builder()
                        .userId(userRoles.getId().getUserId())
                        .roleId(userRoles.getId().getRoleId())
                        .build())
                .assignedAt(userRoles.getAssignedAt())
                .build();
    }
}
