package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

public class PasswordConfig implements IFeature {
    @Override
    public FeatureName getName() {
        return FeatureName.PASSWORD;
    }
}
