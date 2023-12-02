package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;

import lombok.Data;

@Data
public class AppChallengeFlowDto {
    private boolean sendRreq;
    private String authValue;
    private CRES cres;
    private InstitutionUIParams institutionUIParams;
}
