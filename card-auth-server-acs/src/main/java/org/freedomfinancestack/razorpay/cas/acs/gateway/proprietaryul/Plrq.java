package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import lombok.Data;

@Data
public class Plrq {
    String messageType;
    String acsTransID;
    String messageVersion;
    String p_messageVersion;
    PFormValuesBRW p_formValues_BRW;
    PFormValuesAPP p_formValues_APP;

    @Data
    public static class PFormValuesBRW {
        String action;
        String correctFormData;
        String incorrectFormData;
        String cancelFormData;
    }

    @Data
    public static class PFormValuesAPP {
        String correctChallengeData;
        String incorrectChallengeData;
        String correctChallengeData2;
        String incorrectChallengeData2;
        String correctInfoContinueHTML;
    }
}
