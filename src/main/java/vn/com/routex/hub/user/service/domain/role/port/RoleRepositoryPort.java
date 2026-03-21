package vn.com.routex.hub.user.service.domain.role.port;

import vn.com.routex.hub.user.service.domain.role.model.Roles;

import java.util.Optional;

public interface RoleRepositoryPort {

    Optional<Roles> findById(String id);

    Optional<Roles> findByCode(String code);
}
