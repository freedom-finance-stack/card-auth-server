package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.Authentication;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;

/**
 * AuthenticationService interface
 *
 * @param <T>
 */
public interface AuthenticationService<T extends Authentication> {

    public void preAuthenticate(T t) throws ACSException;

    public AuthResponse authenticate(T t) throws ACSException;
}
