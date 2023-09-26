package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@code OTPAuthenticationServiceImpl} class is an implementation of the {@link
 * AuthenticationService} interface. This service is responsible for authenticating the user using
 * the One Time Password (OTP) and generating the Authentication Response.
 *
 * @author jaydeepRadadiya
 * @since 1.0.0
 * @version 1.0.0
 */
@Service("OTPAuthenticationService")
@Slf4j
public class OTPAuthenticationServiceImpl implements AuthenticationService {

    @Override
    public void preAuthenticate(AuthenticationDto authentication) throws ThreeDSException {
        // Generation
        // send
    }

    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ThreeDSException {
        // otp fetch from DB
        // validation
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(true);
        return authResponse;
    }
}
