package org.freedomfinancestack.razorpay.cas.acs.dto;

import lombok.Data;

@Data
public class AuthResponse {
    boolean authenticated;
    String displayMessage;
}
