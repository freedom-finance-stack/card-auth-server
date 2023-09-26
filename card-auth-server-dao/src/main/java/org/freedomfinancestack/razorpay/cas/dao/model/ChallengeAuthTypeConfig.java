package org.freedomfinancestack.razorpay.cas.dao.model;

import java.math.BigDecimal;

import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ChallengeAuthTypeConfig implements IFeature {

    @SerializedName(value = "purchase_amount_threshold")
    BigDecimal threshold;

    @SerializedName(value = "above_threshold_auth_type")
    AuthType thresholdAuthType;

    @SerializedName(value = "default_auth_type")
    AuthType defaultAuthType;

    @Override
    public FeatureName getName() {
        return FeatureName.CHALLENGE_AUTH_TYPE;
    }
}
