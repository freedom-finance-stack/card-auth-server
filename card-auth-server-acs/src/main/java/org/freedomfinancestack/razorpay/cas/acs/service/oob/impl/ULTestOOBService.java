package org.freedomfinancestack.razorpay.cas.acs.service.oob.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrqService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrs;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("ULTestOOBService")
@Slf4j
@RequiredArgsConstructor
public class ULTestOOBService implements OOBService {
    private final POrqService porqService;

    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ThreeDSException {
        log.info(
                "Authenticating UL_TEST OOB flow for transaction id: {}",
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
