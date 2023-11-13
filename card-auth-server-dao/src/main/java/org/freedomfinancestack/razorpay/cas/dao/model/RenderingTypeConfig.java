package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;

import com.google.gson.annotations.SerializedName;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class RenderingTypeConfig implements IFeature {

    @SerializedName(value = "acs_interface")
    String acsInterface;

    @SerializedName(value = "acs_ui_template")
    String acsUiTemplate;

    @SerializedName(value = "default_render_option")
    String defaultRenderOption;

    @SerializedName(value = "acs_ui_type")
    String acsUiType;

    @Override
    public FeatureName getName() {
        return FeatureName.RENDERING_TYPE;
    }
}
