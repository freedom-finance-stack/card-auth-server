package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction_message_type_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class TransactionMessageTypeDetail extends BaseEntity {
    @Id
    @Column(name = "id")
    private String id;

    private String message;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "received_timestamp")
    private Timestamp receivedTimestamp;

    @Column(name = "sent_timestamp")
    private Timestamp sentTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    public TransactionMessageTypeDetail(String message, MessageType messageType){
        this.message = message;
        this.messageType = messageType;
    }
}
