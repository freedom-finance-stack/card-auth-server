package org.freedomfinancestack.razorpay.cas.contract;

import java.io.Serializable;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CVReq extends ThreeDSObject implements Serializable {
    String transactionId;
    String authVal;
    boolean resendChallenge;
    boolean cancelChallenge;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.CVReq;
    }
}
