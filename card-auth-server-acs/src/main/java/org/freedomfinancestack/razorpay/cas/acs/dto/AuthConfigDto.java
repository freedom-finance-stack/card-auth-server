package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.dao.model.ChallengeAttemptConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.ChallengeAuthTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.PasswordConfig;

import lombok.Data;

@Data
public class AuthConfigDto {
    ChallengeAttemptConfig challengeAttemptConfig;
    ChallengeAuthTypeConfig challengeAuthTypeConfig;
    OtpConfig otpConfig;
    PasswordConfig passwordConfig;
}
