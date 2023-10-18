package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_reference_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@ToString(exclude = {"transaction"})
@Builder
public class TransactionReferenceDetail extends BaseEntity<String> {

    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "threeds_server_transaction_id")
    private String threedsServerTransactionId;

    @Column(name = "threeds_server_reference_number")
    private String threedsServerReferenceNumber;

    @Column(name = "ds_transaction_id")
    private String dsTransactionId;

    @Column(name = "ds_url")
    private String dsUrl;

    @Column(name = "notification_url")
    private String notificationUrl;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;

    public TransactionReferenceDetail(
            String threedsServerTransactionId,
            String threedsServerReferenceNumber,
            String dsTransactionId,
            String dsUrl,
            String notificationUrl) {

        this.threedsServerTransactionId = threedsServerTransactionId;
        this.threedsServerReferenceNumber = threedsServerReferenceNumber;
        this.dsTransactionId = dsTransactionId;
        this.dsUrl = dsUrl;
        this.notificationUrl = notificationUrl;
    }
}
