package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "institution_acs_url")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is null")
public class InstitutionAcsUrl extends BaseEntity {

    @EmbeddedId
    private InstitutionAcsUrlPK institutionAcsUrlPK;

    @Column(name = "challenge_url", length = 400)
    private String challengeUrl;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
    @Override
    public Object getId() {
        return institutionAcsUrlPK;
    }

}

