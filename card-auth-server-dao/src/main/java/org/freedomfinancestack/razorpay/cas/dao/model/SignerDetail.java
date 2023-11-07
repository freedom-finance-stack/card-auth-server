package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "signer_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class SignerDetail extends BaseEntity<SignerDetailPK> {

    @EmbeddedId private SignerDetailPK signerDetailPK;

    @Column(name = "keystore")
    private String keystore;

    @Column(name = "keypass")
    private String keypass;

    @Column(name = "debit_cert_key")
    private String debitCertKey;

    @Column(name = "credit_cert_key")
    private String creditCertKey;

    @Column(name = "debit_signer_key")
    private String debitSignerKey;

    @Column(name = "credit_signer_key")
    private String creditSignerKey;

    @Column(name = "root_cert_key")
    private String rootCertKey;

    @Column(name = "inter_cert_key")
    private String interCertKey;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Override
    public SignerDetailPK getId() {
        return signerDetailPK;
    }
}
