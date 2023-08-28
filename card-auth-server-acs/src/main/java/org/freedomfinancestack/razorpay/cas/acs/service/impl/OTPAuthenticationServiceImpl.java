package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OTPAuthenticationServiceImpl implements AuthenticationService {

    @Override
    public void preAuthenticate(AuthenticationDto authentication) throws ACSException {}

    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ACSException {
        return null;
    }
}
