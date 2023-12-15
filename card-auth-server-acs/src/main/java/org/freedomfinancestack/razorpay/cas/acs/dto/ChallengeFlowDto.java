package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.springframework.web.ErrorResponse;

import lombok.Data;

@Data
public class ChallengeFlowDto {

    private boolean sendRreq;
    private String authValue;

    // todo resolve this and combine flow
    private CdRes cdRes;

    private ErrorResponse errorResponse;
    private CRES cres;

    String threeDSSessionData;
    String notificationUrl;

    private InstitutionUIParams institutionUIParams;
}
