package com.razorpay.acs.dao.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "transaction_reference_detail")
@Data
@ToString(exclude = {"transaction"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
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

    public TransactionReferenceDetail(String threedsServerTransactionId, String threedsServerReferenceNumber, String dsTransactionId) {
        this.threedsServerTransactionId = threedsServerTransactionId;
        this.threedsServerReferenceNumber = threedsServerReferenceNumber;
        this.dsTransactionId = dsTransactionId;
    }


}
