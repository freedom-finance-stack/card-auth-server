package com.razorpay.ffs.cas.dao.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;

    public TransactionReferenceDetail(
            String threedsServerTransactionId,
            String threedsServerReferenceNumber,
            String dsTransactionId) {
        this.threedsServerTransactionId = threedsServerTransactionId;
        this.threedsServerReferenceNumber = threedsServerReferenceNumber;
        this.dsTransactionId = dsTransactionId;
    }
}
