package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.ACSRenderingType;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant.MESSAGE_TYPE_VERSION;

@Data
@EqualsAndHashCode(callSuper = true)
public class RREQ extends ThreeDSObject {

    // All Required Fields
    private String threeDSServerTransID;

    private String acsTransID;

    private String dsTransID;

    private String interactionCounter;

    private String messageCategory;

    private String messageType = MessageType.RReq.toString();

    private String messageVersion = MESSAGE_TYPE_VERSION;

    private String transStatus;

    // All Optional Fields

    // All Conditional Fields
    private String authenticationMethod;

    private String authenticationType;

    private String authenticationValue;

    private String challengeCancel;

    private String eci;

    private String transStatusReason;

    private List<MessageExtension> messageExtension;

    private ACSRenderingType acsRenderingType;

    private String sdkTransID;

    private String whiteListStatus;

    private String whiteListStatusSource;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.RReq;
    }
}
