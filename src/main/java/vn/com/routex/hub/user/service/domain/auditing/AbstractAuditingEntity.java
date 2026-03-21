package vn.com.routex.hub.user.service.domain.auditing;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractAuditingEntity {

    private OffsetDateTime createdAt;

    private String createdBy;

    private String updatedBy;

    private OffsetDateTime updatedAt;
}
