package com.razorpay.ffs.cas.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonParser;

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
        } else if (this.id != null && id.length() > 64) {
            return false;
        } else if (this.getData() != null) {
            try {
                JsonParser.parseString(this.getData());
            } catch (Exception e) {
                return false;
            }
            return this.getData().length() <= 8059;
        }
        return true;
    }
}
