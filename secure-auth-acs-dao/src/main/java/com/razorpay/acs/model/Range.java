package com.razorpay.acs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

@Entity
@Table(name = "ranges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE ranges SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE ranges SET deleted_at = now() ")
public class Range {
    @Id
    private String id;

    @Column(name = "range_group_id")
    private String rangeGroupId;

    @Column(name = "start_range")
    private String startRange;

    @Column(name = "end_range")
    private String endRange;

    @Column(name = "attmept_allowed")
    private Byte attemptAllowed;

    @Column(name = "block_on_exceed_attempt", nullable = false)
    private Byte blockOnExceedAttempt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_flag", nullable = false)
    private RiskFlag riskFlag;

    @Column(name = "instrument_desc")
    private String instrumentDesc;

    @Column(name = "whitelisting_allowed")
    private String whitelistingAllowed;

    @Column(name = "card_store_type")
    private Byte cardStoreType;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    // Enums
    public enum Status {
        Active,
        Inactive
    }

    public enum CardType {
        Credit,
        Debit,
        Prepaid
    }

    public enum RiskFlag {
        Frictionless,
        Challenge
    }
}
