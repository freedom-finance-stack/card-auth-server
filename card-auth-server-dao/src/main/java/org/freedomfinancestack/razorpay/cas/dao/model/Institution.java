package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "institution")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class Institution extends BaseEntity<String> {

    @Id private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "iso_country_code")
    private Short isoCountryCode;

    @Column(name = "timezone")
    private String timezone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InstitutionStatus status;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @OneToOne(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private InstitutionMeta institutionMeta;

    public void setInstitutionMeta(InstitutionMeta institutionMeta) {
        this.institutionMeta = institutionMeta;
        institutionMeta.setInstitution(this);
    }
}
