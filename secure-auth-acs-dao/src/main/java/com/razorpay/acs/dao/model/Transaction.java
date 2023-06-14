package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.annotation.SoftDeleteRead;
import com.razorpay.acs.dao.enums.FlowType;
import com.razorpay.acs.dao.enums.MessageCategory;
import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    @Column(name = "message_version")
    private String  messageVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_category")
    private MessageCategory messageCategory;

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

    @OneToOne(mappedBy = "transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private TransactionReferenceDetail transactionReferenceDetail;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionBrowserDetail transactionBrowserDetail;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionMerchant transactionMerchant;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionCardDetail transactionCardDetail;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TransactionMessageTypeDetail> transactionMessageTypeDetail; // todo lazy loading not working

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionPurchaseDetail transactionPurchaseDetail;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionSdkDetail transactionSdkDetail;


    public void setTransactionReferenceDetail(TransactionReferenceDetail transactionReferenceDetail) {
        this.transactionReferenceDetail = transactionReferenceDetail;
        transactionReferenceDetail.setTransaction(this);
    }

    public void setTransactionMerchant(TransactionMerchant transactionMerchant) {
        this.transactionMerchant = transactionMerchant;
        transactionMerchant.setTransaction(this);
    }

    public void setTransactionCardDetail(TransactionCardDetail transactionCardDetail) {
        this.transactionCardDetail = transactionCardDetail;
        transactionCardDetail.setTransaction(this);
    }

    public void setTransactionSdkDetail(TransactionSdkDetail transactionSdkDetail){
        this.transactionSdkDetail = transactionSdkDetail;
        transactionSdkDetail.setTransaction(this);
    }

    public void setTransactionPurchaseDetail(TransactionPurchaseDetail transactionPurchaseDetail){
        this.transactionPurchaseDetail = transactionPurchaseDetail;
        transactionPurchaseDetail.setTransaction(this);
    }

    public void setTransactionMessageTypeDetail( List<TransactionMessageTypeDetail> transactionMessageTypeDetails ){
        this.transactionMessageTypeDetail = transactionMessageTypeDetails;
        transactionMessageTypeDetails.forEach(transactionMessageTypeDetail -> transactionMessageTypeDetail.setTransaction(this));
    }

    public void setTransactionBrowserDetail(TransactionBrowserDetail transactionBrowserDetail){
        this.transactionBrowserDetail = transactionBrowserDetail;
        transactionBrowserDetail.setTransaction(this);
    }
}
