package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpConfig implements IFeature {
    @SerializedName("length")
    int length;

    @Override
    public FeatureName getName() {
        return FeatureName.OTP;
    }
}
