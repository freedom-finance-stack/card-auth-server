package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBService;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBServiceLocator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("OOBAuthenticationService")
@Slf4j
@RequiredArgsConstructor
public class OOBAuthenticationService implements AuthenticationService {
    private final OOBServiceLocator oobServiceLocator;

    @Override
    public void preAuthenticate(AuthenticationDto authentication) throws ThreeDSException {
        log.info(
                "Pre Authenticating OOB flow for transaction id: {}",
                authentication.getTransaction().getId());
    }

    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ThreeDSException {
        log.info(
                "Authenticating OOB flow for transaction id: {}",
                authentication.getTransaction().getId());

        if (authentication.getAuthConfigDto() == null
                || authentication.getAuthConfigDto().getOobConfig() == null
                || authentication.getAuthConfigDto().getOobConfig().getOobType() == null) {
            throw new OperationNotSupportedException(
                    InternalErrorCode.AUTH_CONFIG_NOT_PRESENT, "Missing OOB Type");
        }

        OOBService oobService =
                oobServiceLocator.locateOOBService(
                        authentication.getAuthConfigDto().getOobConfig().getOobType());

        return oobService.authenticate(authentication);
    }
}
