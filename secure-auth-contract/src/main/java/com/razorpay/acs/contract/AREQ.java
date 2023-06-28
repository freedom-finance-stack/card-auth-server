package com.razorpay.acs.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AREQ extends ThreeDSObject {

  // All Required Fields
  @JsonProperty("threeDSRequestorURL")
  private String threeDSRequestorURL;

  @JsonProperty("threeDSCompInd")
  private String threeDSCompInd;

  @JsonProperty("threeDSRequestorAuthenticationInd")
  private String threeDSRequestorAuthenticationInd;

  @JsonProperty("threeDSRequestorID")
  private String threeDSRequestorID;

  @JsonProperty("threeDSRequestorName")
  private String threeDSRequestorName;

  @JsonProperty("threeDSServerRefNumber")
  private String threeDSServerRefNumber;

  @JsonProperty("threeDSServerTransID")
  private String threeDSServerTransID;

  @JsonProperty("threeDSServerURL")
  private String threeDSServerURL;

  @JsonProperty("threeRIInd")
  private String threeRIInd;

  @JsonProperty("acquirerBIN")
  private String acquirerBIN;

  @JsonProperty("acquirerMerchantID")
  private String acquirerMerchantID;

  @JsonProperty("browserAcceptHeader")
  private String browserAcceptHeader;

  @JsonProperty("browserJavaEnabled")
  private String browserJavaEnabled;

  @JsonProperty("browserLanguage")
  private String browserLanguage;

  @JsonProperty("browserColorDepth")
  private String browserColorDepth;

  @JsonProperty("browserScreenHeight")
  private String browserScreenHeight;

  @JsonProperty("browserScreenWidth")
  private String browserScreenWidth;

  @JsonProperty("browserTZ")
  private String browserTZ;

  @JsonProperty("browserUserAgent")
  private String browserUserAgent;

  @JsonProperty("acctNumber")
  private String acctNumber;

  @JsonProperty("deviceChannel")
  private String deviceChannel;

  @JsonProperty("deviceRenderOptions")
  private DeviceRenderOptions deviceRenderOptions;

  @JsonProperty("mcc")
  private String mcc;

  @JsonProperty("merchantCountryCode")
  private String merchantCountryCode;

  @JsonProperty("merchantName")
  private String merchantName;

  @JsonProperty("messageCategory")
  private String messageCategory;

  @JsonProperty("messageType")
  private String messageType;

  @JsonProperty("messageVersion")
  private String messageVersion;

  @JsonProperty("notificationURL")
  private String notificationURL;

  @JsonProperty("purchaseAmount")
  private String purchaseAmount;

  @JsonProperty("purchaseCurrency")
  private String purchaseCurrency;

  @JsonProperty("purchaseExponent")
  private String purchaseExponent;

  @JsonProperty("purchaseDate")
  private String purchaseDate;

  @JsonProperty("sdkAppID")
  private String sdkAppID;

  @JsonProperty("sdkEphemPubKey")
  // private String sdkEphemPubKey;
  private EphemPubKey sdkEphemPubKey;

  @JsonProperty("sdkMaxTimeout")
  private String sdkMaxTimeout;

  @JsonProperty("sdkReferenceNumber")
  private String sdkReferenceNumber;

  @JsonProperty("sdkTransID")
  private String sdkTransID;

  // All Optional Fields
  @JsonProperty("threeDSRequestorAuthenticationInfo")
  private ThreeDSRequestorAuthenticationInfo threeDSRequestorAuthenticationInfo;

  @JsonProperty("threeDSRequestorChallengeInd")
  private String threeDSRequestorChallengeInd;

  @JsonProperty("threeDSRequestorPriorAuthenticationInfo")
  private ThreeDSRequestorPriorAuthenticationInfo threeDSRequestorPriorAuthenticationInfo;

  @JsonProperty("addrMatch")
  private String addrMatch;

  @JsonProperty("acctInfo")
  private CardholderAccountInformation acctInfo;

  @JsonProperty("acctID")
  private String acctID;

  @JsonProperty("merchantRiskIndicator")
  private ThreeDSMerchantFeilds merchantRiskIndicator;

  @JsonProperty("threeDSReqAuthMethodInd")
  private String threeDSReqAuthMethodInd;

  // All Conditional Fields
  @JsonProperty("threeDSServerOperatorID")
  private String threeDSServerOperatorID;

  @JsonProperty("acctType")
  private String acctType;

  @JsonProperty("broadInfo")
  private BrodInfo broadInfo;

  @JsonProperty("browserIP")
  private String browserIP;

  @JsonProperty("cardExpiryDate")
  private String cardExpiryDate;

  @JsonProperty("billAddrCity")
  private String billAddrCity;

  @JsonProperty("billAddrCountry")
  private String billAddrCountry;

  @JsonProperty("billAddrLine1")
  private String billAddrLine1;

  @JsonProperty("billAddrLine2")
  private String billAddrLine2;

  @JsonProperty("billAddrLine3")
  private String billAddrLine3;

  @JsonProperty("billAddrPostCode")
  private String billAddrPostCode;

  @JsonProperty("billAddrState")
  private String billAddrState;

  @JsonProperty("email")
  private String email;

  @JsonProperty("homePhone")
  private Phone homePhone;

  @JsonProperty("mobilePhone")
  private Phone mobilePhone;

  @JsonProperty("cardholderName")
  private String cardholderName;

  @JsonProperty("shipAddrCity")
  private String shipAddrCity;

  @JsonProperty("shipAddrCountry")
  private String shipAddrCountry;

  @JsonProperty("shipAddrLine1")
  private String shipAddrLine1;

  @JsonProperty("shipAddrLine2")
  private String shipAddrLine2;

  @JsonProperty("shipAddrLine3")
  private String shipAddrLine3;

  @JsonProperty("shipAddrPostCode")
  private String shipAddrPostCode;

  @JsonProperty("shipAddrState")
  private String shipAddrState;

  @JsonProperty("workPhone")
  private Phone workPhone;

  @JsonProperty("deviceInfo")
  private String deviceInfo;

  @JsonProperty("dsReferenceNumber")
  private String dsReferenceNumber;

  @JsonProperty("dsTransID")
  private String dsTransID;

  @JsonProperty("dsURL")
  private String dsURL;

  @JsonProperty("payTokenInd")
  private String payTokenInd;

  @JsonProperty("purchaseInstalData")
  private String purchaseInstalData;

  @JsonProperty("messageExtension")
  private List messageExtension;

  @JsonProperty("recurringExpiry")
  private String recurringExpiry;

  @JsonProperty("recurringFrequency")
  private String recurringFrequency;

  @JsonProperty("sdkEncData")
  private String sdkEncData;

  @JsonProperty("transType")
  private String transType;

  @JsonProperty("browserJavascriptEnabled")
  private String browserJavascriptEnabled;

  @JsonProperty("whiteListStatus")
  private String whiteListStatus;

  @JsonProperty("whiteListStatusSource")
  private String whiteListStatusSource;

  @JsonProperty("payTokenSource")
  private String payTokenSource;

  @JsonProperty("threeDSRequestorDecMaxTime")
  private String threeDSRequestorDecMaxTime;

  @JsonProperty("threeDSRequestorDecReqInd")
  private String threeDSRequestorDecReqInd;

  private transient String transactionId;
  // -------------------------------------------

  /*@JsonProperty("threeDSRequestorNPAInd")
  private String threeDSRequestorNPAInd;
  @JsonProperty("challengeMandated")
  private String challengeMandated;*/
}
