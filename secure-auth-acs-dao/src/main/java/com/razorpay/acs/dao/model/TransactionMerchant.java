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
@Data
@Table(name = "transaction_merchant")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_merchants SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transaction_merchants SET deleted_at = now() ")
public class TransactionMerchant {
    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "acquirer_merchant_id")
    private String acquirerMerchantId;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_country_code")
    private Short merchantCountryCode;
}
