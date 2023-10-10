package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "institution_acs_url")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class InstitutionAcsUrl extends BaseEntity<InstitutionAcsUrlPK> {

    @EmbeddedId private InstitutionAcsUrlPK institutionAcsUrlPK;

    @Column(name = "challenge_url", length = 400)
    private String challengeUrl;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Override
    public InstitutionAcsUrlPK getId() {
        return institutionAcsUrlPK;
    }
}
