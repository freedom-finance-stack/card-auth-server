package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ChallengeAttemptConfig implements IFeature {

    @SerializedName(value = "attempt_threshold")
    int attemptThreshold;

    @SerializedName(value = "resend_threshold")
    int resendThreshold;

    @SerializedName(value = "block_on_exceed_attempt")
    boolean blockOnExceedAttempt;

    @SerializedName(value = "whitelisting_allowed")
    boolean whitelistingAllowed;

    @Override
    public FeatureName getName() {
        return FeatureName.CHALLENGE_ATTEMPT;
    }
}
