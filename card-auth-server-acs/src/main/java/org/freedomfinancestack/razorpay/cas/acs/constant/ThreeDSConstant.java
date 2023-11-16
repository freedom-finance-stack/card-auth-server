package org.freedomfinancestack.razorpay.cas.acs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreeDSConstant {

    public static final String MESSAGE_VERSION_2_1_0 = "2.1.0";
    public static final String MESSAGE_VERSION_2_2_0 = "2.2.0";
    public static final String[] SUPPORTED_MESSAGE_VERSION = {
        MESSAGE_VERSION_2_1_0, MESSAGE_VERSION_2_2_0
    };

    public static final String[] ALL_VERSIONS_SUPPORTED_ELEMENT = {
        MESSAGE_VERSION_2_1_0, MESSAGE_VERSION_2_2_0
    };

    // Supported Message Types
    public static final String ELEMENT_THREEDS_COMPIND = "threeDSCompInd";
    public static final String ELEMENT_THREEDS_REQUESTOR_ID = "threeDSRequestorID";
    public static final String ELEMENT_THREEDS_REQUESTOR_NAME = "threeDSRequestorName";
    public static final String ELEMENT_THREEDS_REQUESTOR_URL = "threeDSRequestorURL";
    public static final String ELEMENT_THREEDSSERVER_REF_NUMBER = "threeDSServerRefNumber";
    public static final String ELEMENT_THREEDSSERVER_TRANS_ID = "threeDSServerTransID";
    public static final String ELEMENT_THREEDSSERVER_URL = "threeDSServerURL";
    public static final String ELEMENT_ACS_TRANS_ID = "acsTransID";
    public static final String ELEMENT_ACQUIRER_BIN = "acquirerBIN";
    public static final String ELEMENT_ACQUIRER_MERCHANT_ID = "acquirerMerchantID";
    public static final String ELEMENT_BROWSER_ACCEPT_HEADER = "browserAcceptHeader";
    public static final String ELEMENT_BROWSER_JAVA_ENABLED = "browserJavaEnabled";
    public static final String ELEMENT_BROWSER_JAVA_SCRIPT_ENABLED = "browserJavascriptEnabled";
    public static final String ELEMENT_BROWSER_LANGUAGE = "browserLanguage";
    public static final String ELEMENT_BROWSER_COLOR_DEPTH = "browserColorDepth";
    public static final String ELEMENT_BROWSER_SCREEN_HEIGHT = "browserScreenHeight";
    public static final String ELEMENT_BROWSER_SCREEN_WIDTH = "browserScreenWidth";
    public static final String ELEMENT_BROWSER_TIMEZONE = "browserTZ";
    public static final String ELEMENT_BROWSER_USER_AGENT = "browserUserAgent";
    public static final String ELEMENT_CARD_EXPIRY_DATE = "cardExpiryDate";
    public static final String ELEMENT_CHALLENGE_WINDOW_SIZE = "challengeWindowSize";
    public static final String ELEMENT_ACCOUNT_NUMBER = "acctNumber";
    public static final String ELEMENT_DEVICE_CHANNEL = "deviceChannel";
    public static final String ELEMENT_DEVICE_INFO = "deviceInfo";
    public static final String ELEMENT_DEVICE_RENDER_OPTIONS = "deviceRenderOptions";
    public static final String ELEMENT_DS_URL = "dsURL";
    public static final String ELEMENT_MCC = "mcc";
    public static final String ELEMENT_MERCHANT_COUNTRY_CODE = "merchantCountryCode";
    public static final String ELEMENT_MERCHANT_NAME = "merchantName";
    public static final String ELEMENT_MESSAGE_CATEGORY = "messageCategory";
    public static final String ELEMENT_MESSAGE_TYPE = "messageType";
    public static final String ELEMENT_MESSAGE_VERSION = "messageVersion";
    public static final String ELEMENT_NOTIFICATION_URL = "notificationURL";
    public static final String ELEMENT_PURCHASE_AMOUNT = "purchaseAmount";
    public static final String ELEMENT_PURCHASE_CURRENCY = "purchaseCurrency";
    public static final String ELEMENT_PURCHASE_EXOPONENT = "purchaseExponent";
    public static final String ELEMENT_PURCHASE_DATE = "purchaseDate";
    public static final String ELEMENT_SDK_APP_ID = "sdkAppID";
    public static final String ELEMENT_SDK_EPHEM_PUB_KEY = "sdkEphemPubKey";
    public static final String ELEMENT_SDK_REFERANCE_NUMBER = "sdkReferenceNumber";
    public static final String ELEMENT_SDK_TRANS_ID = "sdkTransID";
    public static final String ELEMENT_SDK_MAX_TIMEOUT = "sdkMaxTimeout";
    public static final String ELEMENT_SDK_COUNTER_STOA = "sdkCounterStoA";

    public static final String ELEMENT_THREEDS_REQUESTOR_AUTH_IND =
            "threeDSRequestorAuthenticationInd";
    public static final String ELEMENT_THREEDS_REQUESTOR_AUTH_INFO =
            "threeDSRequestorAuthenticationInfo";
    public static final String ELEMENT_THREEDS_REQUESTOR_AUTH_METHOD = "threeDSReqAuthMethod";
    public static final String ELEMENT_THREEDS_REQUESTOR_AUTH_TIMESTAMP = "threeDSReqAuthTimestamp";
    public static final String ELEMENT_THREEDS_REQUESTOR_AUTH_DATA = "threeDSReqAuthData";
    public static final String ELEMENT_THREEDS_REQUESTOR_CHALLENGEIND =
            "threeDSRequestorChallengeInd";
    public static final String ELEMENT_THREEDS_REQUESTOR_NPAIND = "threeDSRequestorNPAInd";
    public static final String ELEMENT_ACCOUNT_TYPE = "acctType";
    public static final String ELEMENT_ADDRESS_MATCH = "addrMatch";
    public static final String ELEMENT_BROWSER_IP = "browserIP";
    public static final String ELEMENT_ACCOUNT_ID = "acctID";
    public static final String ELEMENT_BILL_ADDRESS_CITY = "billAddrCity";
    public static final String ELEMENT_BILL_ADDRESS_COUNTRY = "billAddrCountry";
    public static final String ELEMENT_BILL_ADDRESS_LINE1 = "billAddrLine1";
    public static final String ELEMENT_BILL_ADDRESS_LINE2 = "billAddrLine2";
    public static final String ELEMENT_BILL_ADDRESS_LINE3 = "billAddrLine3";
    public static final String ELEMENT_BILL_ADDRESS_POSTCODE = "billAddrPostCode";
    public static final String ELEMENT_BILL_ADDRESS_STATE = "billAddrState";
    public static final String ELEMENT_CARDHOLDER_EMAIL = "email";
    public static final String ELEMENT_HOMEPHONE_CC = "homePhone_cc";
    public static final String ELEMENT_HOMEPHONE_SUBSCRIBER = "homePhone_Subscriber";
    public static final String ELEMENT_MOBILEPHONE_CC = "mobilePhone_cc";
    public static final String ELEMENT_MOBILEPHONE_SUBSCRIBER = "mobilePhone_Subscriber";
    public static final String ELEMENT_CARDHOLDER_NAME = "cardholderName";
    public static final String ELEMENT_SHIP_ADDRESS_CITY = "shipAddrCity";
    public static final String ELEMENT_SHIP_ADDRESS_COUNTRY = "shipAddrCountry";
    public static final String ELEMENT_SHIP_ADDRESS_LINE1 = "shipAddrLine1";
    public static final String ELEMENT_SHIP_ADDRESS_LINE2 = "shipAddrLine2";
    public static final String ELEMENT_SHIP_ADDRESS_LINE3 = "shipAddrLine3";
    public static final String ELEMENT_SHIP_ADDRESS_POSTCODE = "shipAddrPostCode";
    public static final String ELEMENT_SHIP_ADDRESS_STATE = "shipAddrState";
    public static final String ELEMENT_WORKPHONE_CC = "workPhone_cc";
    public static final String ELEMENT_WORKPHONE_SUBSCRIBER = "workPhone_Subscriber";
    public static final String ELEMENT_DS_REF_NUMBER = "dsReferenceNumber";
    public static final String ELEMENT_DS_TRANS_ID = "dsTransID";
    public static final String ELEMENT_PAYTOKEN_IND = "payTokenInd";
    public static final String ELEMENT_PURCHASE_INSTALL_DATA = "purchaseInstalData";
    public static final String ELEMENT_SDK_ENC_DATA = "sdkEncData";
    public static final String ELEMENT_TRANSACTION_TYPE = "transType";
    public static final String ELEMENT_ACCTINFO_CHACCAGE_IND = "chAccAgeInd";
    public static final String ELEMENT_ACCTINFO_CHACCDATE = "chAccDate";
    public static final String ELEMENT_ACCTINFO_CHACCCHANGE_IND = "chAccChangeInd";
    public static final String ELEMENT_ACCTINFO_CHACCCHANGE = "chAccChange";
    public static final String ELEMENT_ACCTINFO_CHACCPWCHANGE_IND = "chAccPwChangeInd";
    public static final String ELEMENT_ACCTINFO_CHACCPWCHANGE = "chAccPwChange";
    public static final String ELEMENT_ACCTINFO_SHIPADDRESS_USAGE_IND = "shipAddressUsageInd";
    public static final String ELEMENT_ACCTINFO_SHIPADDRESS_USAGE = "shipAddressUsage";
    public static final String ELEMENT_ACCTINFO_TXN_ACTIVITY_DAY = "txnActivityDay";
    public static final String ELEMENT_ACCTINFO_TXN_ACTIVITY_YEAR = "txnActivityYear";
    public static final String ELEMENT_ACCTINFO_PROVISION_ATTEMPTS_DAY = "provisionAttemptsDay";
    public static final String ELEMENT_ACCTINFO_NB_PURCHASE_ACCOUNT = "nbPurchaseAccount";
    public static final String ELEMENT_ACCTINFO_SUSPICIOUS_ACC_ACTIVITY = "suspiciousAccActivity";
    public static final String ELEMENT_ACCTINFO_SHIPNAME_INDICATOR = "shipNameIndicator";
    public static final String ELEMENT_ACCTINFO_PAYMENT_ACC_INDICATOR = "paymentAccInd";
    public static final String ELEMENT_ACCTINFO_PAYMENT_ACC_AGE = "paymentAccAge";
    public static final String ELEMENT_CHALLENGE_MANDATED = "challengeMandated";
    public static final String ELEMENT_ACS_DEC_CON_IND = "acsDecConInd";
    public static final String ELEMENT_DEVICE_RENDER_OPTIONS_INTERFACE =
            "deviceRenderOptions_Interface";
    public static final String ELEMENT_DEVICE_RENDER_OPTIONS_UI = "deviceRenderOptions_UI";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_SHIPINDICATOR = "shipIndicator";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_DELIVERY_TIMEFRAME =
            "deliveryTimeframe";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_DELIVERY_EMAILADDRESS =
            "deliveryEmailAddress";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_REORDER_ITEMS_IND = "reorderItemsInd";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_PREORDER_PURCHASE_IND =
            "preOrderPurchaseInd";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_PREORDER_DATE = "preOrderDate";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_GIFTCARD_AMOUNT = "giftCardAmount";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_GIFTCARD_CURRENCY = "giftCardCurr";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR_GIFTCARD_COUNT = "giftCardCount";
    public static final String ELEMENT_MESSAGE_EXTENSION_NAME = "messageExtension_name";
    public static final String ELEMENT_MESSAGE_EXTENSION_ID = "messageExtension_id";
    public static final String ELEMENT_MESSAGE_EXTENSION_CRITICALITY_INDICATOR =
            "messageExtension_criticalityIndicator";
    public static final String ELEMENT_MESSAGE_EXTENSION_DATA = "messageExtension_data";

    public static final String ELEMENT_CHALLENGE_CANCEL = "challengeCancel";
    public static final String ELEMENT_CHALLENGE_DATA_ENTRY = "challengeDataEntry";
    public static final String ELEMENT_CHALLENGE_HTML_DATA_ENTRY = "challengeHTMLDataEntry";
    public static final String ELEMENT_OOB_CONTINUE = "oobContinue";
    public static final String ELEMENT_RESEND_CHALLENGE = "resendChallenge";

    // Required data element in RRES
    public static final String ELEMENT_RESULT_STATUS = "resultsStatus";

    public static final String ELEMENT_THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO =
            "threeDSRequestorPriorAuthenticationInfo";

    public static final String ELEMENT_THREEDSSERVER_OPERATOR_ID = "threeDSServerOperatorID";
    public static final String ELEMENT_THREE_RI_IND = "threeRIInd";
    public static final String ELEMENT_ACS_EPHEM_PUB_KEY = "acsEphemPubKey";

    public static final String ELEMENT_BROAD_INFO = "broadInfo";
    public static final String ELEMENT_ACCOUNT_INFO = "acctInfo";
    public static final String ELEMENT_HOMEPHONE = "homePhone";
    public static final String ELEMENT_MOBILEPHONE = "mobilePhone";
    public static final String ELEMENT_WORKPHONE = "workPhone";
    public static final String ELEMENT_MERCHANT_RISKINDICATOR = "merchantRiskIndicator";
    public static final String ELEMENT_MESSAGE_EXTENSION = "messageExtension";
    public static final String ELEMENT_ACS_OPERATOR_ID = "acsOperatorID";
    public static final String ELEMENT_ACS_REFERENCE_NUMBER = "acsReferenceNumber";
    public static final String ELEMENT_ACS_RENDERING_TYPE = "acsRenderingType";
    public static final String ELEMENT_ACS_SIGNED_CONTENT = "acsSignedContent";
    public static final String ELEMENT_ACS_URL = "acsURL";
    public static final String ELEMENT_AUTHENTICATION_TYPE = "authenticationType";
    public static final String ELEMENT_AUTHENTICATION_VALUE = "authenticationValue";
    public static final String ELEMENT_CARDHOLDER_INFO = "cardholderInfo";
    public static final String ELEMENT_ECI = "eci";
    public static final String ELEMENT_TRANS_STATUS = "transStatus";
    public static final String ELEMENT_TRANS_STATUS_REASON = "transStatusReason";
    public static final String ELEMENT_WHITELIST_STATUS = "whiteListStatus";
    public static final String ELEMENT_WHITELIST_STATUS_SOURCE = "whiteListStatusSource";
    public static final String ELEMENT_AUTHENTICATION_METHOD = "authenticationMethod";
    public static final String ELEMENT_RESULTS_STATUS = "resultsStatus";

    public static final String ELEMENT_WHITELISTING_DATA_ENTRY = "whitelistingDataEntry";
    public static final String ELEMENT_THREEDS_REQ_AUTH_METHOD_IND = "threeDSReqAuthMethodInd";
    public static final String ELEMENT_PAYTOKEN_SOURCE = "payTokenSource";

    public static final String ELEMENT_THREEDS_REQUESTOR_DEC_MAX_TIME =
            "threeDSRequestorDecMaxTime";
    public static final String ELEMENT_THREEDS_REQUESTOR_DEC_REQ_IND = "threeDSRequestorDecReqInd";

    public static final String ELEMENT_THREEDS_REQUESTOR_APP_URL = "threeDSRequestorAppURL";

    public static final String ELEMENT_CHALLENGE_NO_ENTRY = "challengeNoEntry";
}
