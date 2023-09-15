package org.freedomfinancestack.razorpay.cas.dao.model;

import javax.persistence.*;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.State;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.StateMachineEntity;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
public class Transaction extends BaseEntity<String>
        implements StateMachineEntity<Phase.PhaseEvent> {
    @Id private String id;

    @Column(name = "institution_id")
    private String institutionId;

    @Column(name = "card_range_id")
    private String cardRangeId;

    @Column(name = "message_version")
    private String messageVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_category")
    private MessageCategory messageCategory;

    @Column(name = "challenge_mandated")
    private boolean challengeMandated;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    @Builder.Default
    private TransactionStatus transactionStatus = TransactionStatus.CREATED;

    @Column(name = "transaction_status_reason")
    private String transactionStatusReason;

    @Column(name = "challenge_cancel_ind")
    private String challengeCancelInd;

    @Enumerated(EnumType.STRING)
    private Phase phase;

    @Column(name = "threeds_session_data")
    private String threedsSessionData;

    @Column(name = "auth_value")
    private String authValue;

    @Column(name = "eci")
    private String eci;

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
    private TransactionCardDetail transactionCardDetail; // todo verify lazy loading not working

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionPurchaseDetail transactionPurchaseDetail;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionSdkDetail transactionSdkDetail;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionCardHolderDetail transactionCardHolderDetail;

    public void setTransactionReferenceDetail(
            TransactionReferenceDetail transactionReferenceDetail) {
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

    public void setTransactionSdkDetail(TransactionSdkDetail transactionSdkDetail) {
        this.transactionSdkDetail = transactionSdkDetail;
        transactionSdkDetail.setTransaction(this);
    }

    public void setTransactionCardHolderDetail(
            TransactionCardHolderDetail transactionCardHolderDetail) {
        this.transactionCardHolderDetail = transactionCardHolderDetail;
        transactionCardHolderDetail.setTransaction(this);
    }

    public void setTransactionPurchaseDetail(TransactionPurchaseDetail transactionPurchaseDetail) {
        this.transactionPurchaseDetail = transactionPurchaseDetail;
        transactionPurchaseDetail.setTransaction(this);
    }

    public void setTransactionBrowserDetail(TransactionBrowserDetail transactionBrowserDetail) {
        this.transactionBrowserDetail = transactionBrowserDetail;
        transactionBrowserDetail.setTransaction(this);
    }

    @Override
    public String EntityName() {
        return "transaction";
    }

    @Override
    public void SetState(State<Phase.PhaseEvent> state) {
        this.setPhase((Phase) state);
    }

    @Override
    public Phase GetState() {
        return this.getPhase();
    }
}
