package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RenderingTypeConfig implements IFeature {
    @SerializedName("default_render_option")
    String defaultRenderOption;

    @SerializedName("acs_ui_type")
    String acsUiType;

    @SerializedName("acs_interface")
    String acsInterface;

    @SerializedName("acs_ui_template")
    String acsUiTemplate;

    @Override
    public FeatureName getName() {
        return FeatureName.ACS_RENDERING_TYPE;
    }
}
