package org.freedomfinancestack.razorpay.cas.contract;

import lombok.Data;

@Data
public class ChallengeSelectInfo {
    private String phone;
    private String mail;
    private String sms;
    private String email;
}
