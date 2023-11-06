package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
// need to add this where clause in all the entities to make soft delete work, as Where is non
// inherited annotation couldn't find easy way
public class CardDetail extends BaseEntity<String> {
    @Id private String id;

    @Column(name = "institution_id", nullable = false)
    private String institutionId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_expiry")
    private String cardExpiry;

    private boolean blocked;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cardholder_id", referencedColumnName = "id")
    @MapsId
    private Cardholder cardholder;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
}
