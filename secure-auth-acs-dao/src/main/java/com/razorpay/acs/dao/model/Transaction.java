package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.annotation.SoftDeleteRead;
import com.razorpay.acs.dao.enums.FlowType;
import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Transaction extends BaseEntity<String> {
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
