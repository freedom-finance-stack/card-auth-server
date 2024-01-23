package org.freedomfinancestack.razorpay.cas.acs.data;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.contract.CREQ;

public class CREQTestData {

    public static final String SAMPLE_threeDSServerTransID = "2ddc9891-d085-411d-b068-933e30de8231";
    public static final String SAMPLE_acsTransID = "279301f0-b090-4dfe-b7dc-c7861ea5c1cc";
    public static final String SAMPLE_messageType = "CReq";
    public static final String SAMPLE_messageVersion = "2.2.0";

    public static CREQ createCREQ(Map<String, Object> changedValues) {
        CREQ creq = new CREQ();

        creq.setThreeDSServerTransID(
                getOrDefault(changedValues, "threeDSServerTransID", SAMPLE_threeDSServerTransID));
        creq.setThreeDSRequestorAppURL(getOrDefault(changedValues, "threeDSRequestorAppURL", null));
        creq.setAcsTransID(getOrDefault(changedValues, "acsTransID", SAMPLE_acsTransID));
        creq.setChallengeWindowSize(getOrDefault(changedValues, "challengeWindowSize", null));
        creq.setMessageType(getOrDefault(changedValues, "messageType", SAMPLE_messageType));
        creq.setMessageVersion(
                getOrDefault(changedValues, "messageVersion", SAMPLE_messageVersion));
        creq.setSdkCounterStoA(getOrDefault(changedValues, "sdkCounterStoA", null));
        creq.setSdkTransID(getOrDefault(changedValues, "sdkTransID", null));
        creq.setChallengeCancel(getOrDefault(changedValues, "challengeCancel", null));
        creq.setChallengeDataEntry(getOrDefault(changedValues, "challengeDataEntry", null));
        creq.setChallengeHTMLDataEntry(getOrDefault(changedValues, "challengeHTMLDataEntry", null));
        creq.setResendChallenge(getOrDefault(changedValues, "resendChallenge", null));
        creq.setChallengeNoEntry(getOrDefault(changedValues, "challengeNoEntry", null));
        creq.setWhitelistingDataEntry(getOrDefault(changedValues, "whitelistingDataEntry", null));
        creq.setOobContinue(getOrDefault(changedValues, "oobContinue", null));

        return creq;
    }

    private static <T> T getOrDefault(Map<String, Object> map, String key, T defaultValue) {
        return map.containsKey(key) ? (T) map.get(key) : defaultValue;
    }
}
