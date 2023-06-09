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
@Table(name = "transaction_reference_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_reference_details SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transaction_reference_details SET deleted_at = now() ")
public class TransactionReferenceDetail extends BaseEntity {
    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "threeds_server_transaction_id")
    private String threedsServerTransactionId;

    @Column(name = "threeds_server_reference_number")
    private String threedsServerReferenceNumber;

    @Column(name = "ds_transaction_id")
    private String dsTransactionId;

}
