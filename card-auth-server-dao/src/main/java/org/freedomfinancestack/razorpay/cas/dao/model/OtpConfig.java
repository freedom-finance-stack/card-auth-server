package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OtpConfig implements IFeature {
    @JsonProperty("length")
    int length;

    @Override
    public FeatureName getName() {
        return FeatureName.OTP;
    }
}
