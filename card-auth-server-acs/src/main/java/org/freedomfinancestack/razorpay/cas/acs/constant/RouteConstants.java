package org.freedomfinancestack.razorpay.cas.acs.constant;

import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.SLASH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstants {

    public static final String VERSION_TWO = "v2";
    public static final String TRANSACTION_ROUTE = SLASH + VERSION_TWO + SLASH + "transaction";
    public static final String AUTHENTICATION_ROUTE = SLASH + "authentication";
    public static final String CHALLENGE_BROWSER_ROUTE = SLASH + "challenge" + SLASH + "browser";
    public static final String CHALLENGE_BROWSER_VALIDATE_ROUTE =
            CHALLENGE_BROWSER_ROUTE + SLASH + "validate";
    public static final String CHALLENGE_BROWSER_URL = TRANSACTION_ROUTE + CHALLENGE_BROWSER_ROUTE;
    public static final String CHALLENGE_BROWSER_VALIDATION_URL =
            TRANSACTION_ROUTE + CHALLENGE_BROWSER_VALIDATE_ROUTE;

    public static final String CHALLENGE_APP_ROUTE = SLASH + "challenge" + SLASH + "app";
    public static final String CHALLENGE_APP_VALIDATE_ROUTE =
            CHALLENGE_APP_ROUTE + SLASH + "validate";

    public static final String CHALLENGE_APP_URL = TRANSACTION_ROUTE + CHALLENGE_APP_ROUTE;
    public static final String CHALLENGE_APP_VALIDATION_URL =
            TRANSACTION_ROUTE + CHALLENGE_APP_VALIDATE_ROUTE;

    public static String getAcsChallengeUrl(
            final @NonNull String hostName, final @NonNull String deviceChannel) {
        if (DeviceChannel.APP.getChannel().equals(deviceChannel)) {
            return hostName + CHALLENGE_APP_URL;
        }
        return hostName + CHALLENGE_BROWSER_URL;
    }

    public static String getAcsChallengeValidationUrl(
            final @NonNull String hostName, final @NonNull String deviceChannel) {
        if (DeviceChannel.APP.getChannel().equals(deviceChannel)) {
            return hostName + CHALLENGE_APP_VALIDATION_URL;
        }
        return hostName + CHALLENGE_BROWSER_VALIDATION_URL;
    }
}
