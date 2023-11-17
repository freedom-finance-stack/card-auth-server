package org.freedomfinancestack.razorpay.cas.acs.validation;

import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;

import lombok.Getter;

@Getter
public enum ThreeDSDataElement {
    DEVICE_CHANNEL(
            ThreeDSConstant.ELEMENT_DEVICE_CHANNEL,
            DeviceChannel.getChannelValues(),
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MESSAGE_CATEGORY(
            ThreeDSConstant.ELEMENT_MESSAGE_CATEGORY,
            MessageCategory.getCategoryValues(),
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_COMPIND(
            ThreeDSConstant.ELEMENT_THREEDS_COMPIND,
            new String[] {"Y", "N", "U"},
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_AUTHENTICATION_IND(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_AUTH_IND,
            new String[] {"01", "02", "03", "04", "05", "06", "07"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_AUTHENTICATION_INFO(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_AUTH_INFO,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_CHALLENGE_IND(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_CHALLENGEIND,
            new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_ID(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_NAME(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_NAME,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_REQUESTOR_URL(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_URL,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_SERVER_REF_NUMBER(
            ThreeDSConstant.ELEMENT_THREEDSSERVER_REF_NUMBER,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_SERVER_OPERATOR_ID(
            ThreeDSConstant.ELEMENT_THREEDSSERVER_OPERATOR_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_SERVER_TRANSACTION_ID(
            ThreeDSConstant.ELEMENT_THREEDSSERVER_TRANS_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    THREEDS_SERVER_URL_2_2_0(
            ThreeDSConstant.ELEMENT_THREEDSSERVER_URL,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    THREEDS_SERVER_URL_2_1_0(
            ThreeDSConstant.ELEMENT_THREEDSSERVER_URL,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_1_0}),

    THREEDS_RI_IND(
            ThreeDSConstant.ELEMENT_THREE_RI_IND,
            new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"},
            new DeviceChannel[] {DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACCT_TYPE(
            ThreeDSConstant.ELEMENT_ACCOUNT_TYPE,
            new String[] {"01", "02", "03"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-PA-Required NPA-Optional
    ACQUIRER_BIN(
            ThreeDSConstant.ELEMENT_ACQUIRER_BIN,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-PA-Required NPA-Optional
    ACQUIRER_MERCHANT_ID(
            ThreeDSConstant.ELEMENT_ACQUIRER_MERCHANT_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ADDRESS_MATCH(
            ThreeDSConstant.ELEMENT_ADDRESS_MATCH,
            new String[] {"Y", "N"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROAD_INFO(
            ThreeDSConstant.ELEMENT_BROAD_INFO,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_ACCEPT_HEADER(
            ThreeDSConstant.ELEMENT_BROWSER_ACCEPT_HEADER,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_IP(
            ThreeDSConstant.ELEMENT_BROWSER_IP,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_JAVA_ENABLED(
            ThreeDSConstant.ELEMENT_BROWSER_JAVA_ENABLED,
            new String[] {"true", "false"},
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_LANGUAGE(
            ThreeDSConstant.ELEMENT_BROWSER_LANGUAGE,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_COLOR_DEPTH(
            ThreeDSConstant.ELEMENT_BROWSER_COLOR_DEPTH,
            new String[] {"1", "4", "8", "15", "16", "24", "32", "48"},
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_SCREEN_HEIGHT(
            ThreeDSConstant.ELEMENT_BROWSER_SCREEN_HEIGHT,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_SCREEN_WIDTH(
            ThreeDSConstant.ELEMENT_BROWSER_SCREEN_WIDTH,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_TZ(
            ThreeDSConstant.ELEMENT_BROWSER_TIMEZONE,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_USER_AGENT(
            ThreeDSConstant.ELEMENT_BROWSER_USER_AGENT,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BROWSER_JAVA_SCRIPT_ENABLED(
            ThreeDSConstant.ELEMENT_BROWSER_JAVA_SCRIPT_ENABLED,
            new String[] {"true", "false"},
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    CARD_EXPIRY_DATE(
            ThreeDSConstant.ELEMENT_CARD_EXPIRY_DATE,
            "YYMM",
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACCT_INFO(
            ThreeDSConstant.ELEMENT_ACCOUNT_INFO,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACCT_NUMBER(
            ThreeDSConstant.ELEMENT_ACCOUNT_NUMBER,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACCT_ID(
            ThreeDSConstant.ELEMENT_ACCOUNT_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_CITY(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_CITY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_COUNTRY(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_COUNTRY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_LINE_1(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_LINE1,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_LINE_2(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_LINE2,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_LINE_3(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_LINE3,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_POST_CODE(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_POSTCODE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    BILL_ADDR_STATE(
            ThreeDSConstant.ELEMENT_BILL_ADDRESS_STATE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    EMAIL(
            ThreeDSConstant.ELEMENT_CARDHOLDER_EMAIL,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    HOME_PHONE(
            ThreeDSConstant.ELEMENT_HOMEPHONE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MOBILE_PHONE(
            ThreeDSConstant.ELEMENT_MOBILEPHONE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    CARDHOLDER_NAME(
            ThreeDSConstant.ELEMENT_CARDHOLDER_NAME,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_CITY(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_CITY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_COUNTRY(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_COUNTRY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_LINE_1(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_LINE1,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_LINE_2(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_LINE2,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_LINE_3(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_LINE3,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_POST_CODE(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_POSTCODE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SHIP_ADDR_STATE(
            ThreeDSConstant.ELEMENT_SHIP_ADDRESS_STATE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    WORK_PHONE(
            ThreeDSConstant.ELEMENT_WORKPHONE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    DEVICE_INFO(
            ThreeDSConstant.ELEMENT_DEVICE_INFO,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    DEVICE_RENDER_OPTIONS(
            ThreeDSConstant.ELEMENT_DEVICE_RENDER_OPTIONS,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    DS_REFERENCE_NUMBER(
            ThreeDSConstant.ELEMENT_DS_REF_NUMBER,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    DS_TRANS_ID(
            ThreeDSConstant.ELEMENT_DS_TRANS_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    DS_URL(
            ThreeDSConstant.ELEMENT_DS_URL,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    PAY_TOKEN_IND(
            ThreeDSConstant.ELEMENT_PAYTOKEN_IND,
            new String[] {"true"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    PURCHASE_INSTAL_DATA_2_2_0(
            ThreeDSConstant.ELEMENT_PURCHASE_INSTALL_DATA,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),
    PURCHASE_INSTAL_DATA_2_1_0(
            ThreeDSConstant.ELEMENT_PURCHASE_INSTALL_DATA,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_1_0}),
    MCC(
            ThreeDSConstant.ELEMENT_MCC,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MERCHANT_COUNTRY_CODE(
            ThreeDSConstant.ELEMENT_MERCHANT_COUNTRY_CODE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MERCHANT_NAME(
            ThreeDSConstant.ELEMENT_MERCHANT_NAME,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MERCHANT_RISK_INDICATOR(
            ThreeDSConstant.ELEMENT_MERCHANT_RISKINDICATOR,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MESSAGE_EXTENSION(
            ThreeDSConstant.ELEMENT_MESSAGE_EXTENSION,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MESSAGE_EXTENSION_CRITICAL_INDICATOR(
            ThreeDSConstant.ELEMENT_MESSAGE_EXTENSION_CRITICALITY_INDICATOR,
            new String[] {"false"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MESSAGE_TYPE(
            ThreeDSConstant.ELEMENT_MESSAGE_TYPE,
            new String[] {"AReq", "CReq", "RRes", "Erro"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    MESSAGE_VERSION(
            ThreeDSConstant.ELEMENT_MESSAGE_VERSION,
            ThreeDSConstant.SUPPORTED_MESSAGE_VERSION,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),
    NOTIFICATION_URL(
            ThreeDSConstant.ELEMENT_NOTIFICATION_URL,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
    PURCHASE_AMOUNT(
            ThreeDSConstant.ELEMENT_PURCHASE_AMOUNT,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
    PURCHASE_CURRENCY(
            ThreeDSConstant.ELEMENT_PURCHASE_CURRENCY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
    PURCHASE_EXPONENT(
            ThreeDSConstant.ELEMENT_PURCHASE_EXOPONENT,
            new String[] {"0", "1", "2"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
    PURCHASE_DATE(
            ThreeDSConstant.ELEMENT_PURCHASE_DATE,
            "yyyyMMddHHmmss",
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_APP_ID(
            ThreeDSConstant.ELEMENT_SDK_APP_ID,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_ENC_DATA(
            ThreeDSConstant.ELEMENT_SDK_ENC_DATA,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_EPHEM_PUB_KEY(
            ThreeDSConstant.ELEMENT_SDK_EPHEM_PUB_KEY,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_MAX_TIMEOUT(
            ThreeDSConstant.ELEMENT_SDK_MAX_TIMEOUT,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_REFERENCE_NUMBER(
            ThreeDSConstant.ELEMENT_SDK_REFERANCE_NUMBER,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_TRANS_ID(
            ThreeDSConstant.ELEMENT_SDK_TRANS_ID,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    TRANS_TYPE(
            ThreeDSConstant.ELEMENT_TRANSACTION_TYPE,
            new String[] {"01", "03", "10", "11", "28"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // ARes Fields
    ACS_CHALLENGE_MANDATED(
            ThreeDSConstant.ELEMENT_CHALLENGE_MANDATED,
            new String[] {"Y", "N"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACS_DEC_CON_IND(
            ThreeDSConstant.ELEMENT_ACS_DEC_CON_IND,
            new String[] {"Y", "N"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    ACS_OPERATOR_ID(
            ThreeDSConstant.ELEMENT_ACS_OPERATOR_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACS_REFERENCE_NUMBER(
            ThreeDSConstant.ELEMENT_ACS_REFERENCE_NUMBER,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special case:-ARes = CONDITIONAL,RReq = REQUIRED
    ACS_RENDERING_TYPE(
            ThreeDSConstant.ELEMENT_ACS_RENDERING_TYPE,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACS_SIGNED_CONTENT(
            ThreeDSConstant.ELEMENT_ACS_SIGNED_CONTENT,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ACS_TRANS_ID(
            ThreeDSConstant.ELEMENT_ACS_TRANS_ID,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-01-APP:see ACS Signed Content,02-BRW:ARes = C
    ACS_URL(
            ThreeDSConstant.ELEMENT_ACS_URL,
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    AUTHENTICATION_TYPE(
            ThreeDSConstant.ELEMENT_AUTHENTICATION_TYPE,
            new String[] {"01", "03"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // A 20-byte value that has been Base64 encoded,giving a 28-byte result.
    AUTHENTICATION_VALUE(
            ThreeDSConstant.ELEMENT_AUTHENTICATION_VALUE,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    CARDHOLDER_INFO(
            ThreeDSConstant.ELEMENT_CARDHOLDER_INFO,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    ECI(
            ThreeDSConstant.ELEMENT_ECI,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Special Case:-01-PA:ARes =REQUIRED,RReq = R,CRes = C 02-NPA:ARes
    // =CONDITIONAL,RReq=CONDITIONAL,CRes=CONDITIONAL
    TRANS_STATUS(
            ThreeDSConstant.ELEMENT_TRANS_STATUS,
            new String[] {"Y", "N", "U", "A", "C", "R"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    TRANS_STATUS_REASON(
            ThreeDSConstant.ELEMENT_TRANS_STATUS_REASON,
            new String[] {
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21"
            },
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // CREQ Fields

    CHALLENGE_CANCEL(
            ThreeDSConstant.ELEMENT_CHALLENGE_CANCEL,
            new String[] {"01", "04", "05", "06", "07", "08"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    CHALLENGE_DATA_ENTRY(
            ThreeDSConstant.ELEMENT_CHALLENGE_DATA_ENTRY,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    CHALLENGE_HTML_DATA_ENTRY(
            ThreeDSConstant.ELEMENT_CHALLENGE_HTML_DATA_ENTRY,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    CHALLENGE_WINDOW_SIZE(
            ThreeDSConstant.ELEMENT_CHALLENGE_WINDOW_SIZE,
            new String[] {"01", "02", "03", "04", "05"},
            new DeviceChannel[] {DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    OOB_CONTINUE(
            ThreeDSConstant.ELEMENT_OOB_CONTINUE,
            new String[] {"true"},
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    RESEND_CHALLENGE(
            ThreeDSConstant.ELEMENT_RESEND_CHALLENGE,
            new String[] {"Y", "N"},
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    SDK_COUNTER_STOA(
            ThreeDSConstant.ELEMENT_SDK_COUNTER_STOA,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // RRes Fields
    RESULTS_STATUS(
            ThreeDSConstant.ELEMENT_RESULTS_STATUS,
            new String[] {"01", "02", "03"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            ThreeDSConstant.ALL_VERSIONS_SUPPORTED_ELEMENT),

    // Added Missing Field
    THREEDS_REQ_AUTH_METHOD_IND(
            ThreeDSConstant.ELEMENT_THREEDS_REQ_AUTH_METHOD_IND,
            new String[] {"01", "02", "03"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    PAY_TOKEN_SOURCE(
            ThreeDSConstant.ELEMENT_PAYTOKEN_SOURCE,
            new String[] {"01", "02"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    WHITE_LIST_STATUS(
            ThreeDSConstant.ELEMENT_WHITELIST_STATUS,
            new String[] {"Y", "N", "E", "P", "R", "U"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    WHITE_LIST_STATUS_SOURCE(
            ThreeDSConstant.ELEMENT_WHITELIST_STATUS_SOURCE,
            new String[] {"01", "02", "03"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    THREEDS_REQUESTOR_DEC_MAX_TIME(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_DEC_MAX_TIME,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    THREEDS_REQUESTOR_DEC_REQ_IND(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_DEC_REQ_IND,
            new String[] {"Y", "N"},
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    WHITE_LISTING_DATA_ENTRY(
            ThreeDSConstant.ELEMENT_WHITELISTING_DATA_ENTRY,
            new String[] {"Y", "N"},
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    THREEDS_REQUESTOR_APP_URL(
            ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_APP_URL,
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    CHALLENGE_NO_ENTRY(
            ThreeDSConstant.ELEMENT_CHALLENGE_NO_ENTRY,
            new String[] {"Y"},
            new DeviceChannel[] {DeviceChannel.APP},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    RECURRING_EXPIRY_2_2_0(
            ThreeDSConstant.ELEMENT_RECURRING_EXPIRY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    RECURRING_EXPIRY_2_1_0(
            ThreeDSConstant.ELEMENT_RECURRING_EXPIRY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_1_0}),

    RECURRING_FREQUENCY_2_2_0(
            ThreeDSConstant.ELEMENT_RECURRING_FREQUENCY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_2_0}),

    RECURRING_FREQUENCY_2_1_0(
            ThreeDSConstant.ELEMENT_RECURRING_FREQUENCY,
            new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
            new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA},
            new String[] {ThreeDSConstant.MESSAGE_VERSION_2_1_0}),

    UNSUPPORTED(null, null);

    // ----------------------------------------------------------

    private final String fieldName; // e.g deviceChannel
    private String acceptedFormat; // e.g YYYYMMDDHHMMSS
    private String[] acceptedValues; // e.g { "Y", "N", "U" }
    private DeviceChannel[] supportedChannel; // e.g APP / BRW / TRI
    private MessageCategory[] supportedCategory; // e.g PA / NPA
    private final String[] supportedMessageVersion; // {"2.1.0", "2.2.0"}

    ThreeDSDataElement(String elementFieldName, String[] supportedMessageVersion) {
        this.fieldName = elementFieldName;
        this.supportedMessageVersion = supportedMessageVersion;
    }

    ThreeDSDataElement(
            String elementFieldName,
            String[] acceptedValues,
            DeviceChannel[] supportedChannel,
            MessageCategory[] supportedCategory,
            String[] supportedMessageVersion) {
        this.fieldName = elementFieldName;
        this.acceptedValues = acceptedValues;
        this.supportedChannel = supportedChannel;
        this.supportedCategory = supportedCategory;
        this.supportedMessageVersion = supportedMessageVersion;
    }

    ThreeDSDataElement(
            String elementFieldName, String[] acceptedValues, String[] supportedMessageVersion) {
        this.fieldName = elementFieldName;
        this.acceptedValues = acceptedValues;
        this.supportedMessageVersion = supportedMessageVersion;
    }

    ThreeDSDataElement(
            String elementFieldName,
            DeviceChannel[] deviceChannels,
            MessageCategory[] messageCategories,
            String[] supportedMessageVersion) {
        this.fieldName = elementFieldName;
        this.supportedChannel = deviceChannels;
        this.supportedCategory = messageCategories;
        this.supportedMessageVersion = supportedMessageVersion;
    }

    ThreeDSDataElement(
            String elementFieldName,
            String acceptedFormat,
            DeviceChannel[] deviceChannels,
            MessageCategory[] messageCategories,
            String[] supportedMessageVersion) {
        this.fieldName = elementFieldName;
        this.acceptedFormat = acceptedFormat;
        this.supportedChannel = deviceChannels;
        this.supportedCategory = messageCategories;
        this.supportedMessageVersion = supportedMessageVersion;
    }
}
