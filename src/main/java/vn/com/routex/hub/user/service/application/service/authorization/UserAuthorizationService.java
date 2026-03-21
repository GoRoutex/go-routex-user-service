package vn.com.routex.hub.user.service.application.service.authorization;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.domain.role.model.Authorities;
import vn.com.routex.hub.user.service.domain.role.port.RoleRepositoryPort;
import vn.com.routex.hub.user.service.domain.role.port.UserRoleRepositoryPort;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserAuthorizationService {

    private final UserRoleRepositoryPort userRoleRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;


    public Set<String> getRoles(String userId) {

        return userRoleRepositoryPort.findByUserId(userId)
                .stream()
                .map(userRole -> roleRepositoryPort.findById(userRole.getId().getRoleId())
                        .orElseThrow())
                .map(role -> "ROLE_" + role.getCode())
                .collect(Collectors.toSet());
    }

    public Set<String> getAuthorities(String userId) {
        return userRoleRepositoryPort.findByUserId(userId)
                .stream()
                .map(userRole -> roleRepositoryPort.findById(userRole.getId().getRoleId()).orElseThrow())
                .flatMap(role -> role.getAuthorities().stream())
                .map(Authorities::getCode)
                .collect(Collectors.toSet());
    }
}
