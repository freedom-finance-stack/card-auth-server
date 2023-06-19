package com.razorpay.acs.dao.contract;

import lombok.Data;

import java.util.List;

import static com.razorpay.acs.dao.contract.constants.EMVCOConstant.MESSAGE_TYPE_ARES;
import static com.razorpay.acs.dao.contract.constants.EMVCOConstant.MESSAGE_TYPE_VERSION;

@Data
public class ARES extends ThreeDSObject {

    //All Required Fields
    private String threeDSServerTransID;

    private String acsReferenceNumber;

    private String acsTransID;

    private String dsReferenceNumber;

    private String dsTransID;

    private String messageType = MESSAGE_TYPE_ARES;

    private String messageVersion = MESSAGE_TYPE_VERSION;

    private String sdkEphemPubKey;

    private String sdkTransID;

    private String transStatus;

    private String acsDecConInd;

    //All Optional Fields
    private String cardholderInfo;



    //All Conditional Fields
    private String acsChallengeMandated;

    private String acsOperatorID;

    private ACSRenderingType acsRenderingType;

    private String acsSignedContent;

    private String acsURL;

    private String authenticationMethod; //todo check expected behaviour and authenticationType

    private String authenticationValue;

    private String broadInfo;

    private String eci;

    private List messageExtension;

    private String transStatusReason;

    private String whiteListStatus;

    private String whiteListStatusSource;
}
