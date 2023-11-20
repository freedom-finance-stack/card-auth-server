package org.freedomfinancestack.razorpay.cas.acs.dto;

import lombok.Data;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfigPK;

@Data
public class AppChallengeFlowDto {
    boolean sendRreq;
    String authValue;
    CRES cres;
    InstitutionUiConfig institutionUiConfig;
    Image psImage;
    Image issuerImage;
    ChallengeSelectInfo[] challengeSelectInfo;
}
