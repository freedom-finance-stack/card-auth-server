package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ChallengeAttemptConfig implements IFeature {

    @SerializedName(value = "attempt_allowed")
    int attemptAllowed;

    @SerializedName(value = "block_on_exceed_OTP_attempt")
    boolean blockOnExceedOtpAttempt;

    @SerializedName(value = "whitelisting_allowed")
    boolean whitelistingAllowed;

    @Override
    public FeatureName getName() {
        return FeatureName.CHALLENGE_ATTEMPT;
    }
}
