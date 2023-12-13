package org.freedomfinancestack.razorpay.cas.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeSelectInfo {
    private String yes;
    private String no;
    private String phone;
    private String mail;
    private String sms;
    private String email;
}
