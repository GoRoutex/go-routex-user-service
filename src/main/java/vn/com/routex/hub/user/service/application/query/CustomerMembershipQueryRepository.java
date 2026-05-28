package vn.com.routex.hub.user.service.application.query;

import vn.com.routex.hub.user.service.application.command.customer.CustomerMembershipView;

import java.util.Optional;

public interface CustomerMembershipQueryRepository {
    Optional<CustomerMembershipView> findMembershipSummaryByUserId(String userId);
}
