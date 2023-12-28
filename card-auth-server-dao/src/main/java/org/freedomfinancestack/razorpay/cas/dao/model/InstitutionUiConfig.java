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
@Table(name = "institution_ui_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class  InstitutionUiConfig extends BaseEntity<InstitutionUiConfigPK> {

    @EmbeddedId private InstitutionUiConfigPK institutionUiConfigPK;

    @Column(name = "challenge_info_header")
    private String challengeInfoHeader;

    @Column(name = "challenge_info_label")
    private String challengeInfoLabel;

    @Column(name = "challenge_info_text")
    private String challengeInfoText;

    @Column(name = "expand_info_label")
    private String expandInfoLabel;

    @Column(name = "expand_info_text")
    private String expandInfoText;

    @Column(name = "submit_authentication_label")
    private String submitAuthenticationLabel;

    @Column(name = "resend_information_label")
    private String resendInformationLabel;

    @Column(name = "why_info_label")
    private String whyInfoLabel;

    @Column(name = "why_info_text")
    private String whyInfoText;

    @Column(name = "display_page")
    private String displayPage;

    @Column(name = "whitelisting_info_text")
    private String whitelistingInfoText;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Override
    public InstitutionUiConfigPK getId() {
        return institutionUiConfigPK;
    }
}
