package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.enums.ACSRenderingType;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ARES extends ThreeDSObject {

    // All Required Fields
    private String threeDSServerTransID;

    private String acsReferenceNumber;

    private String acsTransID;

    private String dsReferenceNumber;

    private String dsTransID;

    private String messageType = MessageType.ARes.toString();

    private String messageVersion = EMVCOConstant.DEFAULT_MESSAGE_TYPE_VERSION;

    private String sdkEphemPubKey;

    private String sdkTransID;

    private String transStatus;

    // All Optional Fields
    private String cardholderInfo;

    // All Conditional Fields
    private String acsChallengeMandated;

    private String acsOperatorID;

    private ACSRenderingType acsRenderingType;

    private String acsSignedContent;

    private String acsURL;

    private String authenticationType;

    private String authenticationValue;

    private String broadInfo;

    private String eci;

    private List<MessageExtension> messageExtension;

    private String transStatusReason;

    /** 3DS Version 2.2.0 additional fields */
    private String acsDecConInd;

    private String whiteListStatus;

    private String whiteListStatusSource;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.ARes;
    }
}
