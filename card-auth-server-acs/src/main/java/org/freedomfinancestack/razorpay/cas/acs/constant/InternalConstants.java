package org.freedomfinancestack.razorpay.cas.acs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InternalConstants {

    public static final String BROWSER = "Browser";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String SPACE = " ";

    public static final String INITIAL_ACS_SDK_COUNTER = "000";
    public static final String COLON_SEPARATOR = " : ";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String PADDED_SYMBOL_X = "X";
    public static final String PADDED_SYMBOL_R = "R";
    public static final Character PADDED_SYMBOL_0 = '0';
    public static final String HYPEN_WITH_SPACE = " - ";
    public static final String AMPERSAND = "&";
    public static final String DOT = ".";

    public static final String LAST_FOUR_DIGIT_PLACEHOLDER = "XXXX";
    public static final String PAD_LEFT = "LEFT";
    public static final String PAD_RIGHT = "RIGHT";
    public static final String NO_OP_HSM = "NoOpHSM";
    public static final Character SYMBOL_F = 'F';
    public static final String PROD = "prod";
    public static final Integer OTP_START_RANGE = 111111;
    public static final Integer OTP_END_RANGE = 999999;

    public static final String CHALLENGE_INFORMATION_TEXT =
            "we have sent you a text message with a code ";
    public static final String CHALLENGE_INFORMATION_MOBILE_TEXT =
            "to your registered mobile number %s";
    public static final String AND = " and ";

    public static final String CHALLENGE_INFORMATION_EMAIL_TEXT =
            "to your registered email address %s";

    public static final String CHALLENGE_INCORRECT_OTP_TEXT = "You have entered incorrect OTP";
    public static final String CHALLENGE_CORRECT_OTP_TEXT = "OTP Authentication Successful";
    public static final String MODEL_ATTRIBUTE_CRES = "cRes";
    public static final String MODEL_ATTRIBUTE_ERRO = "erro";
    public static final String MODEL_ATTRIBUTE_NOTIFICATION_URL = "notificationUrl";
    public static final String MODEL_ATTRIBUTE_THREEDS_SESSION_DATA = "threeDSSessionData";

    public static final String MODEL_ATTRIBUTE_CHALLENGE_VALIDATION_REQUEST = "cVReq";
    public static final String MODEL_ATTRIBUTE_CHALLENGE_DISPLAY_RESPONSE = "cdRes";
    public static final String THREE_RI_WHILE_LIST_STATUS_SOURCE = "03";
    public static final String THREE_RI_IND_WHILE_LIST = "10";

    // Placeholders
    public static final String LAST_FOUR_DIGIT_MOBILE_NUMBER = "LAST_FOUR_DIGIT_MOBILE_NUMBER";
    public static final String MASKED_CARD_NUMBER = "MASKED_CARD_NUMBER";
    public static final String MERCHANT_NAME = "MERCHANT_NAME";
    public static final String AMOUNT_WITH_CURRENCY = "AMOUNT_WITH_CURRENCY";
    public static final String TRANSACTION_DATE = "TRANSACTION_DATE";

    public static final String THREE_DS_REQUESTOR_AUTHENTICATION_IND_02 = "02";
    public static final String THREE_DS_REQUESTOR_AUTHENTICATION_IND_03 = "03";
    public static final int THREE_DS_REQUESTOR_DEC_MAX_TIME_UPPER_LIMIT = 10080;
    public static final int THREE_DS_REQUESTOR_DEC_MAX_TIME_LOWER_LIMIT = 1;
    public static final String OOB_CONTINUE_LABEL = "Continue";
}
