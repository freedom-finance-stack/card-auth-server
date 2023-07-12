package com.razorpay.threeds.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDetailDto {
    private String cardNumber;
    private String cardExpiry;
    private String name;
    private String mobileNumber;
    private String emailId;
    private boolean blocked;
    private String dob;
    private String institutionId;
}
