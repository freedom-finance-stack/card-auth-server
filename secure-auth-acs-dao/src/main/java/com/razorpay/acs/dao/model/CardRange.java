package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.enums.CardStoreType;
import com.razorpay.acs.dao.enums.CardType;
import com.razorpay.acs.dao.enums.AuthType;
import com.razorpay.acs.dao.enums.CardRangeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "card_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class CardRange extends BaseEntity<String>  {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "range_group_id", referencedColumnName = "id")
    @MapsId
    private RangeGroup rangeGroup ;

    @Column(name = "start_range")
    private Long startRange;

    @Column(name = "end_range")
    private Long endRange;

    @Column(name = "attempt_allowed")
    private Byte attemptAllowed;

    @Column(name = "block_on_exceed_attempt", nullable = false)
    private Byte blockOnExceedAttempt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardRangeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType;

    @Column(name = "description")
    private String description;

    @Column(name = "whitelisting_allowed")
    private String whitelistingAllowed;

    @Column(name = "network_code")
    private Byte networkCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_store_type")
    private CardStoreType cardStoreType;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;


}
