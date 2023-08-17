package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.Authentication;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OTPAuthenticationServiceImpl implements AuthenticationService<Authentication> {

    @Override
    public void preAuthenticate(Authentication authentication) throws ACSException {}

    @Override
    public AuthResponse authenticate(Authentication authentication) throws ACSException {
        return null;
    }
}
