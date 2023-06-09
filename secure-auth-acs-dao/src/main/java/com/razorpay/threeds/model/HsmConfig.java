package com.razorpay.threeds.model;

import javax.persistence.*;

import com.razorpay.acs.model.HSMConfigPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "hsm_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE hsm_config SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE hsm_config SET deleted_at = now() ")
public class HsmConfig {

    @EmbeddedId
    private HSMConfigPK hsmConfigPK;

    @Column(name = "version")
    private String version;

    @Column(name = "hsm_slot_id")
    private String hsmSlotId;

    @Column(name = "hsm_usr_pwd")
    private String hsmUsrPwd;

    @Column(name = "hsm_root_cert_key")
    private String hsmRootCertKey;

    @Column(name = "hsm_inter_cert_key")
    private String hsmInterCertKey;

    @Column(name = "hsm_credit_cert_key")
    private String hsmCreditCertKey;

    @Column(name = "hsm_credit_signer_key")
    private String hsmCreditSignerKey;

    @Column(name = "hsm_credit_cvv_cvc_key")
    private String hsmCreditCvvCvcKey;

    @Column(name = "hsm_debit_cert_key")
    private String hsmDebitCertKey;

    @Column(name = "hsm_debit_signer_key")
    private String hsmDebitSignerKey;

    @Column(name = "hsm_debit_cvv_cvc_key")
    private String hsmDebitCvvCvcKey;

    @Column(name = "keystore")
    private String keystore;

    @Column(name = "keypass")
    private String keypass;

    @Column(name = "usr_terminal")
    private String usrTerminal;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

}
