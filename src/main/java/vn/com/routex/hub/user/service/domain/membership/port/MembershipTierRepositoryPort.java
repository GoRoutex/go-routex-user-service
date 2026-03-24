package vn.com.routex.hub.user.service.domain.membership.port;

import vn.com.routex.hub.user.service.domain.membership.model.MembershipTier;

import java.util.Optional;

public interface MembershipTierRepositoryPort {
    Optional<MembershipTier> findById(String id);

    Optional<MembershipTier> findByPriorityLevel(int i);
}
