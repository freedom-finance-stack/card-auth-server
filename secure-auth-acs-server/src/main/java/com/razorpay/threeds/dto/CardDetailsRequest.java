package com.razorpay.threeds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDetailsRequest {
    String institutionId;
    String cardNumber;
}
