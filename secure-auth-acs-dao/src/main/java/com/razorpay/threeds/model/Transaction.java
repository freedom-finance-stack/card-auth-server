package com.razorpay.acs.model;

import java.sql.Timestamp;
import javax.persistence.*;

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
@Table(name = "transactions")
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

    // Enums
    public enum FlowType {
        FRICTIONLESS,
        CHALLENGE
    }

    public enum TransactionStatus {
        SUCCESS,
        FAILED,
        UNABLE_TO_AUTHENTICATE,
        ATTEMPT,
        CHALLENGE_REQUIRED,
        CHALLENGE_REQUIRED_DECOUPLED,
        REJECTED,
        INFORMATIONAL
    }

    public enum Phase {
        AREQ,
        ARES,
        CREQ,
        RETRY_CREQ,
        CRES,
        RREQ,
        REDIRECT,
        RESEND_OTP,
        AUTH_INITIATE,
        GENERATE_OTP,
        AUTH_RESULT,
        SEAMLESS_GENERATE_OTP,
        VERIFY_OTP,
        RRES,
        ERROR
    }
}
