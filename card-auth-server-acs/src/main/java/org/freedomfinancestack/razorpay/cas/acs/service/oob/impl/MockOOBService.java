package org.freedomfinancestack.razorpay.cas.acs.service.oob.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("MockOOBService")
@Slf4j
@RequiredArgsConstructor
public class MockOOBService implements OOBService {
    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ThreeDSException {
        log.info(
                "Authenticating Mock OOB flow for transaction id: {}",
                authentication.getTransaction().getId());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(true);
        authResponse.setDisplayMessage(InternalConstants.CHALLENGE_CORRECT_OTP_TEXT);
        return authResponse;
    }
}
