package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;

import lombok.Data;

/**
 * The {@code ValidateChallengeResponse} class represents a response object for challenge
 * validation.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class ValidateChallengeResponse {
    CRES cRes;
    String notificationUrl;
    String threeDSSessionData;
}
