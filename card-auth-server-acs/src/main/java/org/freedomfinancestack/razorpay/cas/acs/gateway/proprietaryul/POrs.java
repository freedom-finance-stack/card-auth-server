package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class POrs {
    String messageType;
    boolean p_isOobSuccessful;
    String messageVersion;
    String p_messageVersion;
}
