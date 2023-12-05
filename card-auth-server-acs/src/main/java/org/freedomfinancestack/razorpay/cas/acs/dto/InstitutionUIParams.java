package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionUIParams {
    private Image psImage;
    private Image issuerImage;
    private ChallengeSelectInfo[] challengeSelectInfo;
    private String challengeInfoHeader;
    private String challengeInfoLabel;
    private String challengeInfoText;
    private String expandInfoLabel;
    private String expandInfoText;
    private String submitAuthenticationLabel;
    private String resendInformationLabel;
    private String whyInfoLabel;
    private String whyInfoText;
    private String displayPage;
    private String whitelistingInfoText;
    private String oobContinueLabel;
}
