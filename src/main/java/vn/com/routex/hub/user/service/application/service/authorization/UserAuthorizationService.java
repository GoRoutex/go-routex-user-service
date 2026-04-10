package vn.com.routex.hub.user.service.application.service.authorization;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.user.service.domain.merchant.MerchantUserStatus;
import vn.com.routex.hub.user.service.domain.role.model.Authorities;
import vn.com.routex.hub.user.service.domain.role.model.Roles;
import vn.com.routex.hub.user.service.domain.role.port.RoleRepositoryPort;
import vn.com.routex.hub.user.service.domain.role.port.UserRoleRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.merchant.repository.MerchantUserJpaRepository;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class UserAuthorizationService {

    private final UserRoleRepositoryPort userRoleRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final MerchantUserJpaRepository merchantUserJpaRepository;

    public Set<String> getRoles(String userId) {
        return getUserRoles(userId).stream()
                .map(role -> "ROLE_" + role.getCode())
                .collect(Collectors.toSet());
    }

    public Set<String> getAuthorities(String userId) {
        return getUserRoles(userId).stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(Authorities::getCode)
                .collect(Collectors.toSet());
    }

    public Optional<String> getMerchantId(String userId) {
        return merchantUserJpaRepository.findByUserIdAndStatus(userId, MerchantUserStatus.ACTIVE)
                .stream()
                .map(merchantUser -> merchantUser.getMerchantId())
                .filter(Objects::nonNull)
                .filter(merchantId -> !merchantId.isBlank())
                .findFirst();
    }

    private Set<Roles> getUserRoles(String userId) {
        Stream<Roles> defaultRoles = userRoleRepositoryPort.findByUserId(userId)
                .stream()
                .map(userRole -> roleRepositoryPort.findById(userRole.getId().getRoleId()).orElseThrow());

        Stream<Roles> merchantRoles = merchantUserJpaRepository.findByUserIdAndStatus(userId, MerchantUserStatus.ACTIVE)
                .stream()
                .map(merchantUser -> roleRepositoryPort.findByCode(merchantUser.getRoleCode()).orElseThrow());

        return Stream.concat(defaultRoles, merchantRoles)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
