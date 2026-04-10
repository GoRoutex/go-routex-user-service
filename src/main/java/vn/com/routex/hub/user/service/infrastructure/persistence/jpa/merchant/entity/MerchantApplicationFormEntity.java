package vn.com.routex.hub.user.service.infrastructure.persistence.jpa.merchant.entity;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.user.service.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.hub.user.service.domain.merchant.ApplicationFormContact;
import vn.com.routex.hub.user.service.domain.merchant.ApplicationFormOwner;
import vn.com.routex.hub.user.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.user.service.infrastructure.persistence.jpa.shared.entity.AbstractAuditingJpaEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "MERCHANT_APPLICATION_FORM")
public class MerchantApplicationFormEntity extends AbstractAuditingJpaEntity {

    @Id
    private String id;
    private String displayName;
    private String legalName;
    @Embedded
    private ApplicationFormContact contact;
    @Embedded
    private ApplicationFormBankInfo bankInfo;
    @Embedded
    private ApplicationFormOwner ownerInfo;
    private String approvedBy;
    private OffsetDateTime approvedAt;
    private String businessLicenseUrl;
    private String businessLicense;
    private String city;
    private String country;
    private String description;
    private String district;
    private String formCode;
    private String merchantId;
    private String merchantName;
    private String postalCode;
    private String province;
    private String rejectedBy;
    private String rejectionReason;
    @Enumerated(EnumType.STRING)
    private ApplicationFormStatus status;
    private OffsetDateTime submittedAt;
    private String submittedBy;
    private String taxCode;
    private String slug;
}
