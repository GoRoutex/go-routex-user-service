package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.role.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "USER_ROLES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesJpaEntity {

    @EmbeddedId
    private UserRoleJpaId id;

    @Column(name = "ASSIGNED_AT")
    private OffsetDateTime assignedAt;
}
