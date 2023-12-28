package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrqService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrs;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("OOBAuthenticationService")
@Slf4j
@RequiredArgsConstructor
public class OOBAuthenticationService implements AuthenticationService {
    private final POrqService porqService;

    @Override
    public void preAuthenticate(AuthenticationDto authentication) throws ThreeDSException {
        log.info(
                "Pre Authenticating OOB flow for transaction id: {}",
                authentication.getTransaction().getId());
    }

    // TODO: create a new locator for type of OOB
    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ThreeDSException {
        log.info(
                "Authenticating OOB flow for transaction id: {}",
                authentication.getTransaction().getId());
        POrs pors =
                porqService.sendPOrq(
                        authentication.getTransaction().getId(),
                        authentication
                                .getTransaction()
                                .getTransactionReferenceDetail()
                                .getThreedsServerTransactionId(),
                        authentication.getTransaction().getMessageVersion());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(pors.isP_isOobSuccessful());

        if (pors.isP_isOobSuccessful())
            authResponse.setDisplayMessage(InternalConstants.CHALLENGE_CORRECT_OTP_TEXT);
        else authResponse.setDisplayMessage(InternalConstants.CHALLENGE_INCORRECT_OTP_TEXT);

        return authResponse;
    }
}
