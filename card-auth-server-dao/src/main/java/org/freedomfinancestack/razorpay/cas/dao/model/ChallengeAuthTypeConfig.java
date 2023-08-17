package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChallengeAuthTypeConfig implements IFeature {

    @JsonProperty(value = "threshold")
    long threshold;

    @JsonProperty(value = "threshold_auth_type")
    AuthType thresholdAuthType;

    @JsonProperty(value = "default_auth_type")
    AuthType defaultAuthType;

    @Override
    public FeatureName getName() {
        return FeatureName.CHALLENGE_AUTH_TYPE;
    }
}
