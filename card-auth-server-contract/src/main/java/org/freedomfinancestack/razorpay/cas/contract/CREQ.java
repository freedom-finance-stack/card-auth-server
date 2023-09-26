package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CREQ extends ThreeDSObject {

    private String threeDSServerTransID;

    private String threeDSRequestorAppURL;

    private String acsTransID;

    private String challengeWindowSize;

    private String messageType;

    private String messageVersion;

    private String sdkCounterStoA;

    private String sdkTransID;

    // All Optional Fields - No optional fields available

    // All Conditional Fields
    private String challengeCancel;

    private String challengeDataEntry;

    private String challengeHTMLDataEntry;

    private List<MessageExtension> messageExtension;

    private String resendChallenge;

    private String challengeNoEntry;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.CReq;
    }
}
