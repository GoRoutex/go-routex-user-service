package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.domain.role.model.UserRoles;
import vn.com.routex.hub.user.service.domain.role.port.UserRoleRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.repository.UserRoleJpaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRoleRepositoryAdapter implements UserRoleRepositoryPort {

    private final UserRoleJpaRepository userRoleJpaRepository;
    private final RolePersistenceMapper rolePersistenceMapper;

    @Override
    public UserRoles save(UserRoles userRoles) {
        return rolePersistenceMapper.toDomain(userRoleJpaRepository.save(rolePersistenceMapper.toJpaEntity(userRoles)));
    }

    @Override
    public List<UserRoles> findByUserId(String userId) {
        return userRoleJpaRepository.findByIdUserId(userId).stream()
                .map(rolePersistenceMapper::toDomain)
                .toList();
    }
}
