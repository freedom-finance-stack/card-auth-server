package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_card_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_card_details SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transaction_card_details SET deleted_at = now() ")
public class TransactionCardDetail {
    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "cardholder_name")
    private String cardholderName;

    @Column(name = "card_expiry")
    private String cardExpiry;

    @Column(name = "network_code", nullable = false)
    private Byte networkCode;
}
