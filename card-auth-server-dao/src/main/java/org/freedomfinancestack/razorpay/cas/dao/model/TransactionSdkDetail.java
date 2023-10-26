package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_sdk_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@ToString(exclude = {"transaction"})
@Builder
public class TransactionSdkDetail extends BaseEntity<String> {
    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "sdk_transaction_id")
    private String sdkTransactionId;

    @Column(name = "acs_ui_type")
    private String acsUiType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;
}
