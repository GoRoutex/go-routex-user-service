package vn.com.routex.hub.user.service.interfaces.model.internal.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.hub.user.service.domain.customer.model.CustomerStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class InternalCustomerResponses {

    private InternalCustomerResponses() {
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerData {
        private String id;
        private String userId;
        private String fullName;
        private CustomerStatus status;
        private Integer totalTrips;
        private BigDecimal tripPoints;
        private BigDecimal totalSpent;
        private OffsetDateTime lastBookingAt;
        private OffsetDateTime lastTripAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerListData {
        private List<CustomerData> items;
    }
}
