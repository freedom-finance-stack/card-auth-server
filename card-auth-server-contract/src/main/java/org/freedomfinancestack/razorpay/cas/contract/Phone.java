package org.freedomfinancestack.razorpay.cas.contract;

import lombok.Data;

@Data
public class Phone implements Validatable {

    private String cc;

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
