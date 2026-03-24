package vn.com.routex.hub.user.service.domain.history.model;

import lombok.AllArgsConstructor;
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
public class MembershipHistory extends AbstractAuditingEntity {

    private String id;
    private String userId;
    private String previousTierId;
    private String currentTierId;
    private MembershipAction type;
    private Integer triggerPoints;
    private BigDecimal totalSpent;
    private String reason;
}
