package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AREQ extends ThreeDSObject {

    // All Required Fields
    private String threeDSRequestorURL;

    private String threeDSCompInd;

    private String threeDSRequestorAuthenticationInd;

    private String threeDSRequestorID;

    private String threeDSRequestorName;

    private String threeDSServerRefNumber;

    private String threeDSServerTransID;

    private String threeDSServerURL;

    private String threeRIInd;

    private String acquirerBIN;

    private String acquirerMerchantID;

    private String browserAcceptHeader;

    private String browserJavaEnabled;

    private String browserLanguage;

    private String browserColorDepth;

    private String browserScreenHeight;

    private String browserScreenWidth;

    private String browserTZ;

    private String browserUserAgent;

    private String acctNumber;

    private String deviceChannel;

    private DeviceRenderOptions deviceRenderOptions;

    private String mcc;

    private String merchantCountryCode;

    private String merchantName;

    private String messageCategory;

    private String messageType;

    private String messageVersion;

    private String notificationURL;

    private String purchaseAmount;

    private String purchaseCurrency;

    private String purchaseExponent;

    private String purchaseDate;

    private String sdkAppID;

    private EphemPubKey sdkEphemPubKey;

    private String sdkMaxTimeout;

    private String sdkReferenceNumber;

    private String sdkTransID;

    // All Optional Fields
    private ThreeDSRequestorAuthenticationInfo threeDSRequestorAuthenticationInfo;

    private String threeDSRequestorChallengeInd;

    private ThreeDSRequestorPriorAuthenticationInfo threeDSRequestorPriorAuthenticationInfo;

    private String addrMatch;

    private CardholderAccountInformation acctInfo;

    private String acctID;

    private ThreeDSMerchantFeilds merchantRiskIndicator;

    // All Conditional Fields
    private String threeDSServerOperatorID;

    private String acctType;

    private BrodInfo broadInfo;

    private String browserIP;

    private String cardExpiryDate;

    private String billAddrCity;

    private String billAddrCountry;

    private String billAddrLine1;

    private String billAddrLine2;

    private String billAddrLine3;

    private String billAddrPostCode;

    private String billAddrState;

    private String email;

    private Phone homePhone;

    private Phone mobilePhone;

    private String cardholderName;

    private String shipAddrCity;

    private String shipAddrCountry;

    private String shipAddrLine1;

    private String shipAddrLine2;

    private String shipAddrLine3;

    private String shipAddrPostCode;

    private String shipAddrState;

    private Phone workPhone;

    private String deviceInfo;

    private String dsReferenceNumber;

    private String dsTransID;

    private String dsURL;

    private String payTokenInd;

    private String purchaseInstalData;

    private List<MessageExtension> messageExtension;

    private String recurringExpiry;

    private String recurringFrequency;

    private String sdkEncData;

    private String transType;

    /** 3DS Version 2.2.0 additional fields */
    private String threeDSReqAuthMethodInd;

    private String threeDSRequestorDecMaxTime;

    private String threeDSRequestorDecReqInd;

    private String browserJavascriptEnabled;

    private String payTokenSource;

    private String whiteListStatus;

    private String whiteListStatusSource;

    private transient String transactionId;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.AReq;
    }
}
