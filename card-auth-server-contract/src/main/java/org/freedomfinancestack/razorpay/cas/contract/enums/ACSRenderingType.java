package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ACSRenderingType {

    private String acsInterface;
    private String acsUiTemplate;
}
