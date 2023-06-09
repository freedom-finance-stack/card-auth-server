package com.razorpay.threeds.model;

import java.sql.Timestamp;
import javax.persistence.*;

import com.razorpay.threeds.enums.FlowType;
import com.razorpay.threeds.enums.Phase;
import com.razorpay.threeds.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

@Entity
@Data
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transactions SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transactions SET deleted_at = now() ")
public class Transaction {
    @Id
    private String id;

    @Column(name = "institution_id")
    private String institutionId;

    @Column(name = "message_category")
    private String messageCategory;

    @Column(name = "message_version")
    private String messageVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "flow_type")
    private FlowType flowType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_status_reason")
    private String transactionStatusReason;

    @Enumerated(EnumType.STRING)
    private Phase phase;

    @Column(name = "threeds_session_data")
    private String threedsSessionData;

    @Column(name = "auth_value")
    private String authValue;

    @Column(name = "device_channel")
    private String deviceChannel;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "interaction_count")
    private Integer interactionCount;

    @Column(name = "error_code")
    private String errorCode;

}
