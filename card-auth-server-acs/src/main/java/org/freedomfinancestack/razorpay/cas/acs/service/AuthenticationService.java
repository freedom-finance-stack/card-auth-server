package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;

/**
 * AuthenticationService interface
 *
 * @param <T>
 */
public interface AuthenticationService {

    public void preAuthenticate(AuthenticationDto authenticationDto) throws ACSException;

    public AuthResponse authenticate(AuthenticationDto authenticationDto) throws ACSException;
}
