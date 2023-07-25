package com.razorpay.ffs.cas.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Phone implements Validatable {

    @JsonProperty("cc")
    private String cc;

    @JsonProperty("subscriber")
    private String subscriber;

    public boolean isValid() {

        if (this.cc != null && (this.cc.length() < 1 || this.cc.length() > 3)) {
                return false;
        }

        if (this.subscriber != null) {
            return this.subscriber.length() <= 15;
        }

        return true;
    }
}
