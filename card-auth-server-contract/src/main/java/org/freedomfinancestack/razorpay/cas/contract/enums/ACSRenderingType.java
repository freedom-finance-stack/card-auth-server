package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.Data;

@Data
public class ACSRenderingType {

    private String acsInterface;
    private String acsUiTemplate;

    public ACSRenderingType(String acsInterface, String acsUiTemplate) {
        this.setAcsInterface(acsInterface);
        this.setAcsUiTemplate(acsUiTemplate);
    }
}
