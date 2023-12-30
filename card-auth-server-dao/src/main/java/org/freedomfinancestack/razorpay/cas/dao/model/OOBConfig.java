package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OOBConfig implements IFeature {
    @SerializedName(value = "oob_type")
    OOBType oobType;

    @Override
    public FeatureName getName() {
        return FeatureName.OOB;
    }
}
