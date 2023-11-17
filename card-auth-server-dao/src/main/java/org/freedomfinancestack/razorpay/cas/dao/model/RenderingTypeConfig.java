package org.freedomfinancestack.razorpay.cas.dao.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class RenderingTypeConfig {

    @SerializedName(value = "acs_interface")
    String acsInterface;

    @SerializedName(value = "acs_ui_template")
    List<String> acsUiTemplate;

    @SerializedName(value = "preference")
    Integer preference;
}
