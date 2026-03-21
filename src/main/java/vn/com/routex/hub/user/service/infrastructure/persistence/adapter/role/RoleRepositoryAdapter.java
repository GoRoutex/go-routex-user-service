package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.role.model.Roles;
import vn.com.routex.hub.user.service.domain.role.port.RoleRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.repository.RolesJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RolesJpaRepository rolesJpaRepository;
    private final RolePersistenceMapper rolePersistenceMapper;

    @Override
    public Optional<Roles> findById(String id) {
        return rolesJpaRepository.findById(id).map(rolePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Roles> findByCode(String code) {
        return rolesJpaRepository.findByCode(code).map(rolePersistenceMapper::toDomain);
    }
}
