package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChallengeAttemptConfig implements IFeature {

    @JsonProperty(value = "attempt_allowed")
    int attemptAllowed;

    @JsonProperty(value = "block_on_exceed_OTP_attempt")
    boolean blockOnExceedOtpAttempt;

    @JsonProperty(value = "whitelisting_allowed")
    boolean whitelistingAllowed;

    @Override
    public FeatureName getName() {
        return FeatureName.CHALLENGE_ATTEMPT;
    }
}
