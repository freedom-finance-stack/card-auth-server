package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

import lombok.Data;

@Data
public class ChallengeFlowDto {

    private boolean sendRreq;
    private String authValue;

    private ThreeDSErrorResponse errorResponse;
    private CRES cres;

    String threeDSSessionData;
    String notificationUrl;
    boolean sendEmptyResponse;
    String encryptedResponse;

    private InstitutionUIParams institutionUIParams;
    private Transaction transaction;
}
