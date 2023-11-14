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
    public static final String COLON_SEPARATOR = " : ";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String PADDED_SYMBOL_X = "X";
    public static final Character PADDED_SYMBOL_0 = '0';
    public static final String HYPEN_WITH_SPACE = " - ";
    public static final String AMPERSAND = "&";
    public static final String DOT = ".";
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

    public static final String CHALLENGE_BRW_VALIDATION_URL =
            "v1/transaction/challenge/browser/validate";
    public static final String CHALLENGE_APP_VALIDATION_URL =
            "v1/transaction/challenge/app/validate";

    public static final String CHALLENGE_BRW_URL = "v1/transaction/challenge/browser";
    public static final String CHALLENGE_APP_URL = "v1/transaction/challenge/app";
}
