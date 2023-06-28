package com.razorpay.threeds.validator.enums;

import com.razorpay.acs.contract.enums.DeviceChannel;
import com.razorpay.acs.contract.enums.MessageCategory;
import com.razorpay.threeds.constant.ThreeDSConstant;
import com.razorpay.threeds.validator.DataLength;

public enum ThreeDSDataElement {
  THREEDS_COMPIND(
      ThreeDSConstant.ELEMENT_THREEDS_COMPIND,
      MessageInclusion.REQUIRED,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N", "U"},
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_AUTHENTICATION_IND(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_AUTH_IND,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03", "04", "05", "06", "07"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_AUTHENTICATION_INFO(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_AUTH_INFO,
      MessageInclusion.OPTIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_CHALLENGE_IND(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_CHALLENGEIND,
      MessageInclusion.OPTIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_ID(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_ID,
      MessageInclusion.REQUIRED,
      new DataLength(35, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_NAME(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_NAME,
      MessageInclusion.REQUIRED,
      new DataLength(40, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO,
      MessageInclusion.OPTIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_URL(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_URL,
      MessageInclusion.REQUIRED,
      new DataLength(2048, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_SERVER_REF_NUMBER(
      ThreeDSConstant.ELEMENT_THREEDSSERVER_REF_NUMBER,
      MessageInclusion.REQUIRED,
      new DataLength(32, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_SERVER_OPERATOR_ID(
      ThreeDSConstant.ELEMENT_THREEDSSERVER_OPERATOR_ID,
      MessageInclusion.CONDITIONAL,
      new DataLength(32, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_SERVER_TRANSACTION_ID(
      ThreeDSConstant.ELEMENT_THREEDSSERVER_TRANS_ID,
      MessageInclusion.REQUIRED,
      new DataLength(36, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_SERVER_URL(
      ThreeDSConstant.ELEMENT_THREEDSSERVER_URL,
      MessageInclusion.REQUIRED,
      new DataLength(2048, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_RI_IND(
      ThreeDSConstant.ELEMENT_THREE_RI_IND,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"},
      new DeviceChannel[] {DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACCT_TYPE(
      ThreeDSConstant.ELEMENT_ACCOUNT_TYPE,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-PA-Required NPA-Optional
  ACQUIRER_BIN(
      ThreeDSConstant.ELEMENT_ACQUIRER_BIN,
      MessageInclusion.CONDITIONAL,
      new DataLength(11, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-PA-Required NPA-Optional
  ACQUIRER_MERCHANT_ID(
      ThreeDSConstant.ELEMENT_ACQUIRER_MERCHANT_ID,
      MessageInclusion.CONDITIONAL,
      new DataLength(35, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ADDRESS_MATCH(
      ThreeDSConstant.ELEMENT_ADDRESS_MATCH,
      MessageInclusion.OPTIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROAD_INFO(
      ThreeDSConstant.ELEMENT_BROAD_INFO,
      MessageInclusion.CONDITIONAL,
      new DataLength(4096, DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_ACCEPT_HEADER(
      ThreeDSConstant.ELEMENT_BROWSER_ACCEPT_HEADER,
      MessageInclusion.REQUIRED,
      new DataLength(2048, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_IP(
      ThreeDSConstant.ELEMENT_BROWSER_IP,
      MessageInclusion.CONDITIONAL,
      new DataLength(45, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_JAVA_ENABLED(
      ThreeDSConstant.ELEMENT_BROWSER_JAVA_ENABLED,
      MessageInclusion.REQUIRED,
      new DataLength(DataLengthType.BOOLEAN),
      new String[] {"true", "false"},
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_LANGUAGE(
      ThreeDSConstant.ELEMENT_BROWSER_LANGUAGE,
      MessageInclusion.REQUIRED,
      new DataLength(8, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_COLOR_DEPTH(
      ThreeDSConstant.ELEMENT_BROWSER_COLOR_DEPTH,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.VARIABLE),
      new String[] {"1", "4", "8", "15", "16", "24", "32", "48"},
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_SCREEN_HEIGHT(
      ThreeDSConstant.ELEMENT_BROWSER_SCREEN_HEIGHT,
      MessageInclusion.REQUIRED,
      new DataLength(6, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_SCREEN_WIDTH(
      ThreeDSConstant.ELEMENT_BROWSER_SCREEN_WIDTH,
      MessageInclusion.REQUIRED,
      new DataLength(6, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_TZ(
      ThreeDSConstant.ELEMENT_BROWSER_TIMEZONE,
      MessageInclusion.REQUIRED,
      new DataLength(5, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_USER_AGENT(
      ThreeDSConstant.ELEMENT_BROWSER_USER_AGENT,
      MessageInclusion.REQUIRED,
      new DataLength(2048, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BROWSER_JAVA_SCRIPT_ENABLED(
      ThreeDSConstant.ELEMENT_BROWSER_JAVA_SCRIPT_ENABLED,
      MessageInclusion.REQUIRED,
      new DataLength(DataLengthType.BOOLEAN),
      new String[] {"true", "false"},
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  CARD_EXPIRY_DATE(
      ThreeDSConstant.ELEMENT_CARD_EXPIRY_DATE,
      MessageInclusion.CONDITIONAL,
      new DataLength(4, DataLengthType.FIXED),
      "YYMM",
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACCT_INFO(
      ThreeDSConstant.ELEMENT_ACCOUNT_INFO,
      MessageInclusion.OPTIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACCT_NUMBER(
      ThreeDSConstant.ELEMENT_ACCOUNT_NUMBER,
      MessageInclusion.REQUIRED,
      new DataLength(19, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACCT_ID(
      ThreeDSConstant.ELEMENT_ACCOUNT_ID,
      MessageInclusion.OPTIONAL,
      new DataLength(64, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_CITY(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_CITY,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_COUNTRY(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_COUNTRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(3, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_LINE_1(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_LINE1,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_LINE_2(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_LINE2,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_LINE_3(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_LINE3,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_POST_CODE(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_POSTCODE,
      MessageInclusion.CONDITIONAL,
      new DataLength(16, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  BILL_ADDR_STATE(
      ThreeDSConstant.ELEMENT_BILL_ADDRESS_STATE,
      MessageInclusion.CONDITIONAL,
      new DataLength(3, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  EMAIL(
      ThreeDSConstant.ELEMENT_CARDHOLDER_EMAIL,
      MessageInclusion.CONDITIONAL,
      new DataLength(254, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  HOME_PHONE(
      ThreeDSConstant.ELEMENT_HOMEPHONE,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MOBILE_PHONE(
      ThreeDSConstant.ELEMENT_MOBILEPHONE,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  CARDHOLDER_NAME(
      ThreeDSConstant.ELEMENT_CARDHOLDER_NAME,
      MessageInclusion.CONDITIONAL,
      new DataLength(45, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_CITY(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_CITY,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_COUNTRY(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_COUNTRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(3, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_LINE_1(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_LINE1,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_LINE_2(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_LINE2,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_LINE_3(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_LINE3,
      MessageInclusion.CONDITIONAL,
      new DataLength(50, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_POST_CODE(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_POSTCODE,
      MessageInclusion.CONDITIONAL,
      new DataLength(16, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SHIP_ADDR_STATE(
      ThreeDSConstant.ELEMENT_SHIP_ADDRESS_STATE,
      MessageInclusion.CONDITIONAL,
      new DataLength(3, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WORK_PHONE(
      ThreeDSConstant.ELEMENT_WORKPHONE,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  DEVICE_CHANNEL(
      ThreeDSConstant.ELEMENT_DEVICE_CHANNEL,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  DEVICE_INFO(
      ThreeDSConstant.ELEMENT_DEVICE_INFO,
      MessageInclusion.REQUIRED,
      new DataLength(64000, DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  DEVICE_RENDER_OPTIONS(
      ThreeDSConstant.ELEMENT_DEVICE_RENDER_OPTIONS,
      MessageInclusion.REQUIRED,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  DS_REFERENCE_NUMBER(
      ThreeDSConstant.ELEMENT_DS_REF_NUMBER,
      MessageInclusion.REQUIRED,
      new DataLength(32, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  DS_TRANS_ID(
      ThreeDSConstant.ELEMENT_DS_TRANS_ID,
      MessageInclusion.REQUIRED,
      new DataLength(36, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  DS_URL(
      ThreeDSConstant.ELEMENT_DS_URL,
      MessageInclusion.CONDITIONAL,
      new DataLength(2048, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  PAY_TOKEN_IND(
      ThreeDSConstant.ELEMENT_PAYTOKEN_IND,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.BOOLEAN),
      new String[] {"true"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  PURCHASE_INSTAL_DATA(
      ThreeDSConstant.ELEMENT_PURCHASE_INSTALL_DATA,
      MessageInclusion.CONDITIONAL,
      new DataLength(3, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MCC(
      ThreeDSConstant.ELEMENT_MCC,
      MessageInclusion.REQUIRED,
      new DataLength(4, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MERCHANT_COUNTRY_CODE(
      ThreeDSConstant.ELEMENT_MERCHANT_COUNTRY_CODE,
      MessageInclusion.REQUIRED,
      new DataLength(3, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MERCHANT_NAME(
      ThreeDSConstant.ELEMENT_MERCHANT_NAME,
      MessageInclusion.REQUIRED,
      new DataLength(40, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MERCHANT_RISK_INDICATOR(
      ThreeDSConstant.ELEMENT_MERCHANT_RISKINDICATOR,
      MessageInclusion.OPTIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MESSAGE_CATEGORY(
      ThreeDSConstant.ELEMENT_MESSAGE_CATEGORY,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "85", "86"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MESSAGE_EXTENSION(
      ThreeDSConstant.ELEMENT_MESSAGE_EXTENSION,
      MessageInclusion.CONDITIONAL,
      new DataLength(81920, DataLengthType.OBJECT),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MESSAGE_TYPE(
      ThreeDSConstant.ELEMENT_MESSAGE_TYPE,
      MessageInclusion.REQUIRED,
      new DataLength(4, DataLengthType.FIXED),
      new String[] {"AReq", "CReq", "RRes", "Erro"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  MESSAGE_VERSION(
      ThreeDSConstant.ELEMENT_MESSAGE_VERSION,
      MessageInclusion.REQUIRED,
      new DataLength(8, DataLengthType.VARIABLE),
      new String[] {"2.1.0", "2.2.0"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  NOTIFICATION_URL(
      ThreeDSConstant.ELEMENT_NOTIFICATION_URL,
      MessageInclusion.REQUIRED,
      new DataLength(256, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
  PURCHASE_AMOUNT(
      ThreeDSConstant.ELEMENT_PURCHASE_AMOUNT,
      MessageInclusion.REQUIRED,
      new DataLength(48, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
  PURCHASE_CURRENCY(
      ThreeDSConstant.ELEMENT_PURCHASE_CURRENCY,
      MessageInclusion.REQUIRED,
      new DataLength(3, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
  PURCHASE_EXPONENT(
      ThreeDSConstant.ELEMENT_PURCHASE_EXOPONENT,
      MessageInclusion.REQUIRED,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"0", "1", "2"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-PA:-REQUIRED,NPA-CONDITIONAL
  PURHASE_DATE(
      ThreeDSConstant.ELEMENT_PURCHASE_DATE,
      MessageInclusion.REQUIRED,
      new DataLength(14, DataLengthType.FIXED),
      "yyyyMMddHHmmss",
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  RECURRING_EXPIRY(
      ThreeDSConstant.ELEMENT_RECURRING_EXPIRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(8, DataLengthType.FIXED),
      "YYYYMMDD",
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  RECURRING_FREQUENCY(
      ThreeDSConstant.ELEMENT_RECURRING_FREQUENCY,
      MessageInclusion.CONDITIONAL,
      new DataLength(4, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_APP_ID(
      ThreeDSConstant.ELEMENT_SDK_APP_ID,
      MessageInclusion.REQUIRED,
      new DataLength(36, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_ENC_DATA(
      ThreeDSConstant.ELEMENT_SDK_ENC_DATA,
      MessageInclusion.CONDITIONAL,
      new DataLength(64000, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_EPHEM_PUB_KEY(
      ThreeDSConstant.ELEMENT_SDK_EPHEM_PUB_KEY,
      MessageInclusion.REQUIRED,
      new DataLength(256, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_MAX_TIMEOUT(
      ThreeDSConstant.ELEMENT_SDK_MAX_TIMEOUT,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_REFERENCE_NUMBER(
      ThreeDSConstant.ELEMENT_SDK_REFERANCE_NUMBER,
      MessageInclusion.REQUIRED,
      new DataLength(32, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_TRANS_ID(
      ThreeDSConstant.ELEMENT_SDK_TRANS_ID,
      MessageInclusion.REQUIRED,
      new DataLength(36, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  TRANS_TYPE(
      ThreeDSConstant.ELEMENT_TRANSACTION_TYPE,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "03", "10", "11", "28"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA}),

  // ARes Fields
  ACS_CHALLENGE_MANDATED(
      ThreeDSConstant.ELEMENT_CHALLENGE_MANDATED,
      MessageInclusion.CONDITIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACS_DEC_CON_IND(
      ThreeDSConstant.ELEMENT_ACS_DEC_CON_IND,
      MessageInclusion.CONDITIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACS_OPERATOR_ID(
      ThreeDSConstant.ELEMENT_ACS_OPERATOR_ID,
      MessageInclusion.CONDITIONAL,
      new DataLength(32, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACS_REFERENCE_NUMBER(
      ThreeDSConstant.ELEMENT_ACS_REFERENCE_NUMBER,
      MessageInclusion.REQUIRED,
      new DataLength(32, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special case:-ARes = CONDITIONAL,RReq = REQUIRED
  ACS_RENDERING_TYPE(
      ThreeDSConstant.ELEMENT_ACS_RENDERING_TYPE,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACS_SIGNED_CONTENT(
      ThreeDSConstant.ELEMENT_ACS_SIGNED_CONTENT,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.JSON),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ACS_TRANS_ID(
      ThreeDSConstant.ELEMENT_ACS_TRANS_ID,
      MessageInclusion.REQUIRED,
      new DataLength(36, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-01-APP:see ACS Signed Content,02-BRW:ARes = C
  ACS_URL(
      ThreeDSConstant.ELEMENT_ACS_URL,
      MessageInclusion.CONDITIONAL,
      new DataLength(2048, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  AUTHENTICATION_TYPE(
      ThreeDSConstant.ELEMENT_AUTHENTICATION_TYPE,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // A 20-byte value that has been Base64 encoded,giving a 28-byte result.
  AUTHENTICATION_VALUE(
      ThreeDSConstant.ELEMENT_AUTHENTICATION_VALUE,
      MessageInclusion.CONDITIONAL,
      new DataLength(28, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  CARDHOLDER_INFO(
      ThreeDSConstant.ELEMENT_CARDHOLDER_INFO,
      MessageInclusion.OPTIONAL,
      new DataLength(128, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  ECI(
      ThreeDSConstant.ELEMENT_ECI,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Special Case:-01-PA:ARes =REQUIRED,RReq = R,CRes = C 02-NPA:ARes
  // =CONDITIONAL,RReq=CONDITIONAL,CRes=CONDITIONAL
  TRANS_STATUS(
      ThreeDSConstant.ELEMENT_TRANS_STATUS,
      MessageInclusion.REQUIRED,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N", "U", "A", "C", "R"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  TRANS_STATUS_REASON(
      ThreeDSConstant.ELEMENT_TRANS_STATUS_REASON,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {
        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
        "16", "17", "18", "19", "20", "21"
      },
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WHITELIST_STATUS(
      ThreeDSConstant.ELEMENT_WHITELIST_STATUS,
      MessageInclusion.OPTIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"Y", "N", "E", "P", "R", "U"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WHITELIST_STATUS_SOURCE(
      ThreeDSConstant.ELEMENT_WHITELIST_STATUS_SOURCE,
      MessageInclusion.CONDITIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"01", "02", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WHITELISTING_DATA_ENTRY(
      ThreeDSConstant.ELEMENT_WHITELISTING_DATA_ENTRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // CREQ Fields

  CHALLENGE_CANCEL(
      ThreeDSConstant.ELEMENT_CHALLENGE_CANCEL,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "04", "05", "06", "07", "08"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  CHALLENGE_DATA_ENTRY(
      ThreeDSConstant.ELEMENT_CHALLENGE_DATA_ENTRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(45, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  CHALLENGE_HTML_DATA_ENTRY(
      ThreeDSConstant.ELEMENT_CHALLENGE_HTML_DATA_ENTRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(256, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  CHALLENGE_WINDOW_SIZE(
      ThreeDSConstant.ELEMENT_CHALLENGE_WINDOW_SIZE,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03", "04", "05"},
      new DeviceChannel[] {DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  OOB_CONTINUE(
      ThreeDSConstant.ELEMENT_OOB_CONTINUE,
      MessageInclusion.CONDITIONAL,
      new DataLength(DataLengthType.BOOLEAN),
      new String[] {"true"},
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  RESEND_CHALLENGE(
      ThreeDSConstant.ELEMENT_RESEND_CHALLENGE,
      MessageInclusion.CONDITIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  SDK_COUNTER_STOA(
      ThreeDSConstant.ELEMENT_SDK_COUNTER_STOA,
      MessageInclusion.REQUIRED,
      new DataLength(3, DataLengthType.FIXED),
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // RRes Fields
  RESULTS_STATUS(
      ThreeDSConstant.ELEMENT_RESULTS_STATUS,
      MessageInclusion.REQUIRED,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // Added Missing Field
  THREEDS_REQ_AUTH_METHOD_IND(
      ThreeDSConstant.ELEMENT_THREEDS_REQ_AUTH_METHOD_IND,
      MessageInclusion.CONDITIONAL,
      new DataLength(02, DataLengthType.FIXED),
      new String[] {"01", "02", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  PAY_TOKEN_SOURCE(
      ThreeDSConstant.ELEMENT_PAYTOKEN_SOURCE,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WHITE_LIST_STATUS(
      ThreeDSConstant.ELEMENT_WHITELIST_STATUS,
      MessageInclusion.OPTIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WHITE_LIST_STATUS_SOURCE(
      ThreeDSConstant.ELEMENT_WHITELIST_STATUS_SOURCE,
      MessageInclusion.CONDITIONAL,
      new DataLength(2, DataLengthType.FIXED),
      new String[] {"01", "02", "03"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_DEC_MAX_TIME(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_DEC_MAX_TIME,
      MessageInclusion.OPTIONAL,
      new DataLength(5, DataLengthType.VARIABLE),
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  THREEDS_REQUESTOR_DEC_REQ_IND(
      ThreeDSConstant.ELEMENT_THREEDS_REQUESTOR_DEC_REQ_IND,
      MessageInclusion.OPTIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP, DeviceChannel.BRW, DeviceChannel.TRI},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  WHITE_LISTING_DATA_ENTRY(
      ThreeDSConstant.ELEMENT_WHITELISTING_DATA_ENTRY,
      MessageInclusion.CONDITIONAL,
      new DataLength(1, DataLengthType.FIXED),
      new String[] {"Y", "N"},
      new DeviceChannel[] {DeviceChannel.APP},
      new MessageCategory[] {MessageCategory.PA, MessageCategory.NPA}),

  // ELEMENT_MESSAGE_CATEGORY(),
  UNSUPPORTED(null, null);

  // ----------------------------------------------------------

  DataLength length; // e.g 2, FIXED/VARIABLE

  String fieldName; // e.g deviceChannel
  String acceptedFormat; // e.g YYYYMMDDHHMMSS

  Object dataType; // e.g String.class
  String[] acceptedValues; // e.g { "Y", "N", "U" }

  MessageInclusion inclusion; // e.g REQUIRED / CONDITIONAL /OPTIONAL
  DeviceChannel[] supportedChannel; // e.g APP / BRW / TRI
  MessageCategory[] supportedCategory; // e.g PA / NPA

  private ThreeDSDataElement(String desc, MessageInclusion inclusion) {
    this.fieldName = desc;
    this.inclusion = inclusion;
  }

  private ThreeDSDataElement(
      String desc,
      MessageInclusion inclusion,
      DataLength length,
      String acceptedFormat,
      DeviceChannel[] supportedChannel,
      MessageCategory[] supportedCategory) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.acceptedFormat = acceptedFormat;
    this.supportedChannel = supportedChannel;
    this.supportedCategory = supportedCategory;
  }

  private ThreeDSDataElement(
      String desc, MessageInclusion inclusion, DataLength length, String[] acceptedValues) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.acceptedValues = acceptedValues;
  }

  private ThreeDSDataElement(
      String desc,
      MessageInclusion inclusion,
      DataLength length,
      String[] acceptedValues,
      DeviceChannel[] supportedChannel,
      MessageCategory[] supportedCategory) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.acceptedValues = acceptedValues;
    this.supportedChannel = supportedChannel;
    this.supportedCategory = supportedCategory;
  }

  private ThreeDSDataElement(
      String desc,
      MessageInclusion inclusion,
      DataLength length,
      DeviceChannel[] supportedChannel,
      MessageCategory[] supportedCategory) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.supportedChannel = supportedChannel;
    this.supportedCategory = supportedCategory;
  }

  private ThreeDSDataElement(
      String desc,
      MessageInclusion inclusion,
      DataLength length,
      Object dataType,
      String[] acceptedValues) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.dataType = dataType;
    this.acceptedValues = acceptedValues;
  }

  private ThreeDSDataElement(
      String desc,
      MessageInclusion inclusion,
      DataLength length,
      Object dataType,
      String acceptedFormat) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.dataType = dataType;
    this.acceptedFormat = acceptedFormat;
  }

  // paysecure changes
  private ThreeDSDataElement(
      String desc, MessageInclusion inclusion, DataLength length, Object dataType) {
    this.fieldName = desc;
    this.inclusion = inclusion;
    this.length = length;
    this.dataType = dataType;
  }

  public String getFieldName() {
    return fieldName;
  }

  public MessageInclusion getInclusion() {
    return inclusion;
  }

  public DataLength getLength() {
    return length;
  }

  public String getAcceptedFormat() {
    return acceptedFormat;
  }

  public Object getDataType() {
    return dataType;
  }

  public String[] getAcceptedValues() {
    return acceptedValues;
  }

  public DeviceChannel[] getSupportedChannel() {
    return supportedChannel;
  }

  public MessageCategory[] getSupportedCategory() {
    return supportedCategory;
  }
}
