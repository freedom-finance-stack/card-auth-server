package org.freedomfinancestack.razorpay.cas.dao.model;

import java.sql.Timestamp;
import javax.persistence.*;

import org.hibernate.annotations.Where;

import lombok.*;

@Entity
@Table(name = "transaction_purchase_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
@ToString(exclude = {"transaction"})
public class TransactionPurchaseDetail extends BaseEntity<String> {
    @Id
    @Column(name = "transaction_id")
    private String id;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;
}
