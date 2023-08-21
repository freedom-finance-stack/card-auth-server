package org.freedomfinancestack.razorpay.cas.dao.model;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_range_group")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Where(clause = "deleted_at is null")
@EqualsAndHashCode(callSuper = true)
public class CardRangeGroup extends BaseEntity<String> {
    @Id private String id;

    private String name;

    private String description;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "institution_id", referencedColumnName = "id")
//    private Institution institution;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
}
