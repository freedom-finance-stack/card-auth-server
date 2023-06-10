package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction_purchase_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class TransactionPurchaseDetail extends BaseEntity {
    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "purchase_amount")
    private String purchaseAmount;

    @Column(name = "purchase_currency")
    private String purchaseCurrency;

    @Column(name = "purchase_exponent")
    private Byte purchaseExponent;

    @Column(name = "purchase_timestamp")
    private Timestamp purchaseTimestamp;

    @Column(name = "pay_token_ind")
    private Boolean payTokenInd;
}
