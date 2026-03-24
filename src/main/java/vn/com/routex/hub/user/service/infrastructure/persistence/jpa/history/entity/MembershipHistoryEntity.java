package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.history.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.history.model.MembershipAction;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.shared.entity.AbstractAuditingJpaEntity;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "MEMBERSHIP_HISTORY")
@Entity
public class MembershipHistoryEntity extends AbstractAuditingJpaEntity {

    @Id
    private String id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "PREVIOUS_TIER_ID")
    private String previousTierId;

    @Column(name = "CURRENT_TIER_ID", nullable = false)
    private String currentTierId;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipAction type;

    @Column(name = "TRIGGER_POINTS", nullable = false)
    private Integer triggerPoints;

    @Column(name = "TOTAL_SPENT",  nullable = false)
    private BigDecimal totalSpent;

    @Column(name = "REASON", nullable = false)
    private String reason;
}
