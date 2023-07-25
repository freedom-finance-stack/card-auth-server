package com.razorpay.ffs.cas.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageExtension implements Validatable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("criticalityIndicator")
    private String criticalityIndicator;

    @JsonProperty("data")
    private String data;

    public boolean isValid() {
        if (this.name != null && name.length() > 64) {
            return false;
        } else return this.id == null || id.length() <= 64;
    }
}
