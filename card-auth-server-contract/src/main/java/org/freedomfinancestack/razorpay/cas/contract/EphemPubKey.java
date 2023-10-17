package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.extensions.validation.validator.Validatable;

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
