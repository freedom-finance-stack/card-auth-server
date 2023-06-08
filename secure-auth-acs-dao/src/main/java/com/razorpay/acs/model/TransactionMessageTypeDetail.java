package com.razorpay.acs.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

@Entity
@Table(name = "transaction_message_type_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_message_type_details SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transaction_message_type_details SET deleted_at = now() ")
public class TransactionMessageTypeDetail {
    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    private String message;

    @Column(name = "received_timestamp")
    private Timestamp receivedTimestamp;

    @Column(name = "sent_timestamp")
    private Timestamp sentTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    // Enums
    public enum MessageType {
        AReq,
        Ares,
        CReq,
        CRes,
        RReq,
        RRes
    }
}
