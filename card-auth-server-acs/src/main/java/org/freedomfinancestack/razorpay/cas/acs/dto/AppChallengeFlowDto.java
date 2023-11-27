package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;

import lombok.Data;

@Data
// TODO create a new InstitutionUIConfig dto
public class AppChallengeFlowDto {
    private boolean sendRreq;
    private String authValue;
    private CRES cres;
    private InstitutionUiConfig institutionUiConfig;
    private Image psImage;
    private Image issuerImage;
    private ChallengeSelectInfo[] challengeSelectInfo;
    private String acsCounterAtoS;
}
