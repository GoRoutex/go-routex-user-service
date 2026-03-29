package vn.com.routex.hub.user.service.domain.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends AbstractAuditingEntity {

    private String id;

    private String passwordHash;

    private String avatarUrl;

    private String address;

    private LocalDate dob;

    private Gender gender;

    private String phoneNumber;

    private String customerMembershipId;

    private String nationalId;

    @Builder.Default
    private Boolean phoneVerified = false;

    @Builder.Default
    private Boolean profileCompleted = false;

    private String email;

    @Builder.Default
    private Boolean emailVerified = false;

    private UserStatus status;

    private String tenantId;

    private String language;

    private String timezone;

    @Builder.Default
    private Integer failLoginCount = 0;

    private OffsetDateTime lastLoginAt;

    private OffsetDateTime lockedUntil;
}
