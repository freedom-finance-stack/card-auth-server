package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.dao.model.*;

import lombok.Data;

/**
 * The {@code AuthConfigDto} class is a data transfer object that contains all the config needed to
 * perform authentication
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
public class AuthConfigDto {
    private ChallengeAttemptConfig challengeAttemptConfig;
    private ChallengeAuthTypeConfig challengeAuthTypeConfig;
    private OtpConfig otpConfig;
    private PasswordConfig passwordConfig;
    private OOBConfig oobConfig;
}
