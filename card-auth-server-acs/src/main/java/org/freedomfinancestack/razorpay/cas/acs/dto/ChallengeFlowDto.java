package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;

import lombok.Data;

@Data
public class ChallengeFlowDto {

    private boolean sendRreq;
    private String authValue;

    // todo resolve this and combine flow
    private CdRes cdRes;

    private CRES cres;
    private InstitutionUIParams institutionUIParams;
}
