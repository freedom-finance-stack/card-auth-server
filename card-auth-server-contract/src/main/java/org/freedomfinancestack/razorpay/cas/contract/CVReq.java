package org.freedomfinancestack.razorpay.cas.contract;

import java.io.Serializable;

import lombok.Data;

@Data
public class CVReq implements Serializable {
    String transactionId;
    String authVal;
    String resendChallenge;
    String cancelChallenge;
}
