package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.role;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.role.model.Authorities;
import vn.com.routex.hub.user.service.domain.role.model.Roles;
import vn.com.routex.hub.user.service.domain.role.model.UserRoleId;
import vn.com.routex.hub.user.service.domain.role.model.UserRoles;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.AuthoritiesJpaEntity;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.RolesJpaEntity;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRoleJpaId;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity.UserRolesJpaEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RolePersistenceMapper {

    public Roles toDomain(RolesJpaEntity rolesJpaEntity) {
        Set<Authorities> authorities = rolesJpaEntity.getAuthorities().stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());

        return Roles.builder()
                .id(rolesJpaEntity.getId())
                .code(rolesJpaEntity.getCode())
                .name(rolesJpaEntity.getName())
                .description(rolesJpaEntity.getDescription())
                .enabled(rolesJpaEntity.getEnabled())
                .authorities(authorities)
                .createdAt(rolesJpaEntity.getCreatedAt())
                .createdBy(rolesJpaEntity.getCreatedBy())
                .updatedAt(rolesJpaEntity.getUpdatedAt())
                .updatedBy(rolesJpaEntity.getUpdatedBy())
                .build();
    }

    public Authorities toDomain(AuthoritiesJpaEntity authoritiesJpaEntity) {
        return Authorities.builder()
                .id(authoritiesJpaEntity.getId())
                .code(authoritiesJpaEntity.getCode())
                .name(authoritiesJpaEntity.getName())
                .description(authoritiesJpaEntity.getDescription())
                .enabled(authoritiesJpaEntity.getEnabled())
                .createdAt(authoritiesJpaEntity.getCreatedAt())
                .createdBy(authoritiesJpaEntity.getCreatedBy())
                .updatedAt(authoritiesJpaEntity.getUpdatedAt())
                .updatedBy(authoritiesJpaEntity.getUpdatedBy())
                .build();
    }

    public UserRoles toDomain(UserRolesJpaEntity userRolesJpaEntity) {
        return UserRoles.builder()
                .id(UserRoleId.builder()
                        .userId(userRolesJpaEntity.getId().getUserId())
                        .roleId(userRolesJpaEntity.getId().getRoleId())
                        .build())
                .assignedAt(userRolesJpaEntity.getAssignedAt())
                .build();
    }

    public UserRolesJpaEntity toJpaEntity(UserRoles userRoles) {
        return UserRolesJpaEntity.builder()
                .id(UserRoleJpaId.builder()
                        .userId(userRoles.getId().getUserId())
                        .roleId(userRoles.getId().getRoleId())
                        .build())
                .assignedAt(userRoles.getAssignedAt())
                .build();
    }
}
