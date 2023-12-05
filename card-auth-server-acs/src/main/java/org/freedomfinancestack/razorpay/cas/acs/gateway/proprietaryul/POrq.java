package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class POrq {
    String messageType;
    String acsTransID;
    String threeDSServerTransID;
    String messageVersion;
    String p_messageVersion;
}
