package org.freedomfinancestack.razorpay.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecoupledAuthenticationResponse {
    boolean isSuccessful;
}
