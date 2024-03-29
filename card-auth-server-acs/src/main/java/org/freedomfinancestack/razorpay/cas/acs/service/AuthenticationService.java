package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;

/**
 * AuthenticationService interface This interface provides methods for authenticating users using
 * various methods such as OTP, password, and OOB. Implementing classes must provide the necessary
 * logic for each authentication method.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
// todo rename to ChallengeAuthenticationService
public interface AuthenticationService {

    /**
     * Performs pre-authentication operations before the actual authentication process.
     *
     * @param authenticationDto The AuthenticationDto containing the necessary data for
     *     authentication.
     * @throws ThreeDSException If an exception occurs during the pre-authentication process.
     */
    void preAuthenticate(AuthenticationDto authenticationDto) throws ThreeDSException;

    /**
     * Authenticates a user using the specified authentication method.
     *
     * @param authenticationDto The AuthenticationDto containing the necessary data for
     *     authentication.
     * @return An AuthResponse indicating the result of the authentication process.
     * @throws ThreeDSException If an exception occurs during the authentication process.
     */
    AuthResponse authenticate(AuthenticationDto authenticationDto) throws ThreeDSException;
}
