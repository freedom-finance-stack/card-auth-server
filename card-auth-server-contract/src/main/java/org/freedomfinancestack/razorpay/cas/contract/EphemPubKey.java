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
        return this.kty != null && this.crv != null && this.x != null && this.y != null;
    }

    private boolean isEmpty() {
        return alg == null
                && kid == null
                && use == null
                && kty == null
                && crv == null
                && x == null
                && y == null;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }

        return "EphemPubKey{"
                + "alg='"
                + alg
                + '\''
                + ", kid='"
                + kid
                + '\''
                + ", use='"
                + use
                + '\''
                + ", kty='"
                + kty
                + '\''
                + ", crv='"
                + crv
                + '\''
                + ", x='"
                + x
                + '\''
                + ", y='"
                + y
                + '\''
                + '}';
    }
}
