package com.razorpay.ffs.cas.contract;

import java.util.List;

import com.razorpay.ffs.cas.contract.constants.EMVCOConstant;
import com.razorpay.ffs.cas.contract.enums.ACSRenderingType;

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

    private String messageType = EMVCOConstant.MESSAGE_TYPE_ARES;

    private String messageVersion = EMVCOConstant.MESSAGE_TYPE_VERSION;

    private String sdkEphemPubKey;

    private String sdkTransID;

    private String transStatus;

    private String acsDecConInd;

    // All Optional Fields
    private String cardholderInfo;

    // All Conditional Fields
    private String acsChallengeMandated;

    private String acsOperatorID;

    private ACSRenderingType acsRenderingType;

    private String acsSignedContent;

    private String acsURL;

    private String authenticationMethod;

    private String authenticationType; // todo check in challenge flow, if this field is required

    private String authenticationValue;

    private String broadInfo;

    private String eci;

    private List messageExtension;

    private String transStatusReason;

    private String whiteListStatus;

    private String whiteListStatusSource;
}
