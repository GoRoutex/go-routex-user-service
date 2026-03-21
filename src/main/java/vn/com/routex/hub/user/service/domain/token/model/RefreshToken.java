package vn.com.routex.hub.user.service.domain.token.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RefreshToken extends AbstractAuditingEntity {

    private String id;

    private String userId;

    private String token;

    private RefreshTokenStatus status;

    private OffsetDateTime issuedAt;

    private OffsetDateTime expiredAt;

    private OffsetDateTime usedAt;

    private OffsetDateTime revokedAt;

}
