package com.razorpay.ffs.cas.contract;

import lombok.Data;

@Data
public class EphemPubKey implements Validatable {

    private String alg;
    private String kid;
    private String use;
    private String kty;
    private String crv;
    private String x;
    private String y;

    public boolean isValid() {
        if (this.kty == null) {
            return false;
        }

        if (this.crv == null) {
            return false;
        }

        if (this.x == null) {
            return false;
        }

        if (this.y == null) {
            return false;
        }

        return true;
    }
}
