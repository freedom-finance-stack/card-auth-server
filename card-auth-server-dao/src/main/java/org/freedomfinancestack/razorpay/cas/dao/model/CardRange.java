package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.CardDetailsStore;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardRangeStatus;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardType;
import org.freedomfinancestack.razorpay.cas.dao.enums.RiskFlag;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class CardRange extends BaseEntity<String> {
    @Id private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    private Institution institution;

    @Column(name = "start_range")
    private Long startRange;

    @Column(name = "end_range")
    private Long endRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardRangeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_flag", nullable = false)
    private RiskFlag riskFlag;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_details_store")
    private CardDetailsStore cardDetailsStore;

    @Column(name = "network_code")
    private Byte networkCode;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
}
