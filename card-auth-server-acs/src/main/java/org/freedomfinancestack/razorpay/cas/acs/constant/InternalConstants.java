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

    public static final String CHALLENGE_INFORMATION_TEXT =
            "we have sent you a text message with a code ";
    public static final String CHALLENGE_INFORMATION_MOBILE_TEXT =
            "to your registered mobile number %s";
    public static final String AND = "and";

    public static final String CHALLENGE_INFORMATION_EMAIL_TEXT =
            "to your registered email address %s";
    public static final String MODEL_ATTRIBUTE_CRES = "cRes";
    public static final String MODEL_ATTRIBUTE_ERRO = "erro";
    public static final String MODEL_ATTRIBUTE_NOTIFICATION_URL = "notificationUrl";
    public static final String MODEL_ATTRIBUTE_THREEDS_SESSION_DATA = "threeDSSessionData";
    public static final String MODEL_ATTRIBUTE_CHALLENGE_RESPONSE = "challengeResponse";
}
