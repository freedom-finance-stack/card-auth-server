package org.freedomfinancestack.razorpay.cas.dao.model;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RenderingTypeConfigList implements IFeature {

    @SerializedName(value = "rendering_type_configs")
    List<RenderingTypeConfig> renderingTypeConfigs;

    @Override
    public FeatureName getName() {
        return FeatureName.RENDERING_TYPE;
    }
}
