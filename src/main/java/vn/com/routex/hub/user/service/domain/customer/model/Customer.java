package vn.com.routex.hub.user.service.domain.customer.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends AbstractAuditingEntity {

    private String id;

    private String userId;

    private CustomerStatus status;

    @Builder.Default
    private Integer totalTrips = 0;

    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal tripPoints = BigDecimal.ZERO;
}
