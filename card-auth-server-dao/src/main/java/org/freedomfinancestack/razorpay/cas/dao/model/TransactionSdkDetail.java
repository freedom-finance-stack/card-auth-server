package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_sdk_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@ToString(exclude = {"transaction"})
@Builder
public class TransactionSdkDetail extends BaseEntity<String> {
    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "sdk_app_id")
    private String sdkAppID;

    @Column(name = "sdk_trans_id")
    private String sdkTransId;

    @Column(name = "sdk_reference_number")
    private String sdkReferenceNumber;

    @Column(name = "acs_interface")
    private String acsInterface;

    @Column(name = "acs_ui_type")
    private String acsUiType;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "acs_secret_key")
    private String acsSecretKey;

    @Column(name = "acs_signed_content")
    private String acsSignedContent;

    // todo remove this field if not needed

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;
}
