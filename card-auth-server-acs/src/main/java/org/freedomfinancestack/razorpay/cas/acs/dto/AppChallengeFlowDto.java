package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;

import lombok.Data;

@Data
public class AppChallengeFlowDto {
    private boolean sendRreq;
    private String authValue;
    private CRES cres;
    private InstitutionUIParams institutionUIParams;
    private Image psImage;
    private Image issuerImage;
    private ChallengeSelectInfo[] challengeSelectInfo;
    private String acsCounterAtoS;
}
