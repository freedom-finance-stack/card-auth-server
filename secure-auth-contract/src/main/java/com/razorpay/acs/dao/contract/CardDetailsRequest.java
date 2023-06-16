package com.razorpay.acs.dao.contract;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDetailsRequest {
    String institutionId;
    String cardNumber;
}
