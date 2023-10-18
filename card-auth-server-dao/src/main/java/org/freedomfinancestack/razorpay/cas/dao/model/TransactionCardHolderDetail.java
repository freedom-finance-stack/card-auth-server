package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_cardholder_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
@ToString(exclude = {"transaction"})
public class TransactionCardHolderDetail extends BaseEntity<String> {
    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;
}
