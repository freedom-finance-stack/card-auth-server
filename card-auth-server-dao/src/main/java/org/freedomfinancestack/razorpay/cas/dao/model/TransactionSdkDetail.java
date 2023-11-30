package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.utils.Util;
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

    @Column(name = "acs_ui_template")
    private String acsUiTemplate;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "acs_secret_key")
    private String acsSecretKey;

    @Column(name = "encryption_method")
    private String encryptionMethod;

    @Column(name = "acs_counter_a_to_s")
    private String acsCounterAtoS;

    @Column(name = "whitelisting_data_entry")
    private String whitelistingDataEntry;

    public String getAcsUiType() {
        if (acsInterface.equals(DeviceInterface.HTML.getValue())) {
            return UIType.HTML_OTHER.getType();
        }
        return acsUiTemplate;
    }

    public void setIncrementedAcsCounterAtoS(int counter) {
        acsCounterAtoS = Util.incrementString(acsCounterAtoS, counter);
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction;
}
