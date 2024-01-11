package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.encryption.Encrypt;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_card_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
@ToString(exclude = {"transaction"})
public class TransactionCardDetail extends BaseEntity<String> {
    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "card_number", nullable = false)
    @Convert(converter = Encrypt.class)
    private String cardNumber;

    @Column(name = "cardholder_name")
    @Convert(converter = Encrypt.class)
    private String cardholderName;

    @Column(name = "network_code", nullable = false)
    private Byte networkCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;
}
