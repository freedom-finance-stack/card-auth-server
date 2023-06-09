package com.razorpay.threeds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction_purchase_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_purchase_details SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transaction_purchase_details SET deleted_at = now() ")
public class TransactionPurchaseDetail {
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
