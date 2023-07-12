package com.razorpay.acs.dao.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

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
