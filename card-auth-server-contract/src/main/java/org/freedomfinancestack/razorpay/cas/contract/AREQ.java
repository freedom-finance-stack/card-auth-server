package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AREQ extends ThreeDSObject {

    // All Required Fields
    @SerializedName(value = "threeDSRequestorURL")
    private String threeDSRequestorURL;

    @SerializedName(value = "threeDSCompInd")
    private String threeDSCompInd;

    @SerializedName(value = "threeDSRequestorAuthenticationInd")
    private String threeDSRequestorAuthenticationInd;

    @SerializedName(value = "threeDSRequestorID")
    private String threeDSRequestorID;

    @SerializedName(value = "threeDSRequestorName")
    private String threeDSRequestorName;

    @SerializedName(value = "threeDSServerRefNumber")
    private String threeDSServerRefNumber;

    @SerializedName(value = "threeDSServerTransID")
    private String threeDSServerTransID;

    @SerializedName(value = "threeDSServerURL")
    private String threeDSServerURL;

    @SerializedName(value = "threeRIInd")
    private String threeRIInd;

    @SerializedName(value = "acquirerBIN")
    private String acquirerBIN;

    @SerializedName(value = "acquirerMerchantID")
    private String acquirerMerchantID;

    @SerializedName(value = "browserAcceptHeader")
    private String browserAcceptHeader;

    @SerializedName(value = "browserJavaEnabled")
    private String browserJavaEnabled;

    @SerializedName(value = "browserLanguage")
    private String browserLanguage;

    @SerializedName(value = "browserColorDepth")
    private String browserColorDepth;

    @SerializedName(value = "browserScreenHeight")
    private String browserScreenHeight;

    @SerializedName(value = "browserScreenWidth")
    private String browserScreenWidth;

    @SerializedName(value = "browserTZ")
    private String browserTZ;

    @SerializedName(value = "browserUserAgent")
    private String browserUserAgent;

    @SerializedName(value = "acctNumber")
    private String acctNumber;

    @SerializedName(value = "deviceChannel")
    private String deviceChannel;

    @SerializedName(value = "deviceRenderOptions")
    private DeviceRenderOptions deviceRenderOptions;

    @SerializedName(value = "mcc")
    private String mcc;

    @SerializedName(value = "merchantCountryCode")
    private String merchantCountryCode;

    @SerializedName(value = "merchantName")
    private String merchantName;

    @SerializedName(value = "messageCategory")
    private String messageCategory;

    @SerializedName(value = "messageType")
    private String messageType;

    @SerializedName(value = "messageVersion")
    private String messageVersion;

    @SerializedName(value = "notificationURL")
    private String notificationURL;

    @SerializedName(value = "purchaseAmount")
    private String purchaseAmount;

    @SerializedName(value = "purchaseCurrency")
    private String purchaseCurrency;

    @SerializedName(value = "purchaseExponent")
    private String purchaseExponent;

    @SerializedName(value = "purchaseDate")
    private String purchaseDate;

    @SerializedName(value = "sdkAppID")
    private String sdkAppID;

    @SerializedName(value = "sdkEphemPubKey")
    private EphemPubKey sdkEphemPubKey;

    @SerializedName(value = "sdkMaxTimeout")
    private String sdkMaxTimeout;

    @SerializedName(value = "sdkReferenceNumber")
    private String sdkReferenceNumber;

    @SerializedName(value = "sdkTransID")
    private String sdkTransID;

    // All Optional Fields
    @SerializedName(value = "threeDSRequestorAuthenticationInfo")
    private ThreeDSRequestorAuthenticationInfo threeDSRequestorAuthenticationInfo;

    @SerializedName(value = "threeDSRequestorChallengeInd")
    private String threeDSRequestorChallengeInd;

    @SerializedName(value = "threeDSRequestorPriorAuthenticationInfo")
    private ThreeDSRequestorPriorAuthenticationInfo threeDSRequestorPriorAuthenticationInfo;

    @SerializedName(value = "addrMatch")
    private String addrMatch;

    @SerializedName(value = "acctInfo")
    private CardholderAccountInformation acctInfo;

    @SerializedName(value = "acctID")
    private String acctID;

    @SerializedName(value = "merchantRiskIndicator")
    private ThreeDSMerchantFeilds merchantRiskIndicator;

    // All Conditional Fields
    @SerializedName(value = "threeDSServerOperatorID")
    private String threeDSServerOperatorID;

    @SerializedName(value = "acctType")
    private String acctType;

    @SerializedName(value = "broadInfo")
    private BrodInfo broadInfo;

    @SerializedName(value = "browserIP")
    private String browserIP;

    @SerializedName(value = "cardExpiryDate")
    private String cardExpiryDate;

    @SerializedName(value = "billAddrCity")
    private String billAddrCity;

    @SerializedName(value = "billAddrCountry")
    private String billAddrCountry;

    @SerializedName(value = "billAddrLine1")
    private String billAddrLine1;

    @SerializedName(value = "billAddrLine2")
    private String billAddrLine2;

    @SerializedName(value = "billAddrLine3")
    private String billAddrLine3;

    @SerializedName(value = "billAddrPostCode")
    private String billAddrPostCode;

    @SerializedName(value = "billAddrState")
    private String billAddrState;

    @SerializedName(value = "email")
    private String email;

    @SerializedName(value = "homePhone")
    private Phone homePhone;

    @SerializedName(value = "mobilePhone")
    private Phone mobilePhone;

    @SerializedName(value = "cardholderName")
    private String cardholderName;

    @SerializedName(value = "shipAddrCity")
    private String shipAddrCity;

    @SerializedName(value = "shipAddrCountry")
    private String shipAddrCountry;

    @SerializedName(value = "shipAddrLine1")
    private String shipAddrLine1;

    @SerializedName(value = "shipAddrLine2")
    private String shipAddrLine2;

    @SerializedName(value = "shipAddrLine3")
    private String shipAddrLine3;

    @SerializedName(value = "shipAddrPostCode")
    private String shipAddrPostCode;

    @SerializedName(value = "shipAddrState")
    private String shipAddrState;

    @SerializedName(value = "workPhone")
    private Phone workPhone;

    @SerializedName(value = "deviceInfo")
    private String deviceInfo;

    @SerializedName(value = "dsReferenceNumber")
    private String dsReferenceNumber;

    @SerializedName(value = "dsTransID")
    private String dsTransID;

    @SerializedName(value = "dsURL")
    private String dsURL;

    @SerializedName(value = "payTokenInd")
    private String payTokenInd;

    @SerializedName(value = "purchaseInstalData")
    private String purchaseInstalData;

    @SerializedName(value = "messageExtension")
    private List<MessageExtension> messageExtension;

    @SerializedName(value = "recurringExpiry")
    private String recurringExpiry;

    @SerializedName(value = "recurringFrequency")
    private String recurringFrequency;

    @SerializedName(value = "sdkEncData")
    private String sdkEncData;

    @SerializedName(value = "transType")
    private String transType;

    /** 3DS Version 2.2.0 additional fields */
    @SerializedName(value = "threeDSReqAuthMethodInd")
    private String threeDSReqAuthMethodInd;

    @SerializedName(value = "threeDSRequestorDecMaxTime")
    private String threeDSRequestorDecMaxTime;

    @SerializedName(value = "threeDSRequestorDecReqInd")
    private String threeDSRequestorDecReqInd;

    @SerializedName(value = "browserJavascriptEnabled")
    private String browserJavascriptEnabled;

    @SerializedName(value = "payTokenSource")
    private String payTokenSource;

    @SerializedName(value = "whiteListStatus")
    private String whiteListStatus;

    @SerializedName(value = "whiteListStatusSource")
    private String whiteListStatusSource;

    private transient String transactionId;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.AReq;
    }
}
