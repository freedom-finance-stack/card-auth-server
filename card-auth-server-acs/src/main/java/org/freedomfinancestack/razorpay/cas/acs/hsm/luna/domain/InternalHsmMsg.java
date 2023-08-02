package org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InternalHsmMsg {
    private String cmdCode;

    private String cvvKey;

    private String cvvData;

    private String cvv;

    private String privateDataKey;

    private String dataTobeSigned;

    private String signature;
}
