package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "institution_meta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
@ToString(exclude = {"institution"})
public class InstitutionMeta extends BaseEntity<String> {

    @Id
    @Column(name = "institution_id")
    private String id;

    @Lob
    @Column(name = "logo_data")
    private byte[] logoData;

    @Column(name = "logo_filename")
    private String logoFilename;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    @MapsId
    private Institution institution;
}
