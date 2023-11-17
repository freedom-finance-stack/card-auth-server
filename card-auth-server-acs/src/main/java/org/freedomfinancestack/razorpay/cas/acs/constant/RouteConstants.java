package org.freedomfinancestack.razorpay.cas.acs.constant;

import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstants {

    public static final String VERSION_TWO = "v2";
    public static final String TRANSACTION_ROUTE = VERSION_TWO + "/transaction";
    public static final String AUTHENTICATION_ROUTE = "/authentication";
    public static final String CHALLENGE_BROWSER_ROUTE = "/challenge/browser";
    public static final String CHALLENGE_BROWSER_VALIDATE_ROUTE = "/challenge/browser/validate";
    public static final String CHALLENGE_APP_ROUTE = "/challenge/app";

    public static String getAcsChallengeUrl(
            final @NonNull String hostName, final @NonNull String deviceChannel) {
        if (DeviceChannel.APP.getChannel().equals(deviceChannel)) {
            return hostName + TRANSACTION_ROUTE + CHALLENGE_APP_ROUTE;
        }
        return hostName + TRANSACTION_ROUTE + CHALLENGE_BROWSER_ROUTE;
    }

    public static String getAcsChallengeValidationUrl(
            final @NonNull String hostName, final @NonNull String deviceChannel) {
        if (DeviceChannel.APP.getChannel().equals(deviceChannel)) {
            return hostName + TRANSACTION_ROUTE + CHALLENGE_APP_ROUTE;
        }
        return hostName + TRANSACTION_ROUTE + CHALLENGE_BROWSER_VALIDATE_ROUTE;
    }
}
