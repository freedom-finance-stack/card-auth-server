package org.ffs.razorpay.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDetailsRequest {
    String institutionId;
    String cardNumber;
}
