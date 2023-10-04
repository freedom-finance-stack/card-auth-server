package org.freedomfinancestack.razorpay.cas.dao.model;

import javax.persistence.*;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_message_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
public class TransactionMessageLog extends BaseEntity<String> {
    @Id
    @Column(name = "id")
    private String id;

    private String message;

    @Column(name = "transaction_id")
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    public TransactionMessageLog(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }
}
