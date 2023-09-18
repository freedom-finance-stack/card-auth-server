package org.freedomfinancestack.razorpay.cas.acs.service;

import java.math.BigDecimal;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.InvalidAuthTypeException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.model.ChallengeAuthTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceLocator {

    private final @Qualifier("OTPAuthenticationService") AuthenticationService
            otpAuthenticationService;

    public AuthenticationService locateTransactionAuthenticationService(
            Transaction transaction, String purchaseAmount, ChallengeAuthTypeConfig authConfig)
            throws InvalidAuthTypeException {
        if (authConfig.getThresholdAuthType() != null
                && !Util.isNullorBlank(purchaseAmount)
                && new BigDecimal(purchaseAmount).compareTo(authConfig.getThreshold()) >= 0) {
            return locateService(authConfig.getThresholdAuthType());
        } else {
            return locateService(authConfig.getDefaultAuthType());
        }
    }

    public AuthenticationService locateService(AuthType authType) throws InvalidAuthTypeException {
        AuthenticationService authenticationService = null;

        switch (authType) {
            case OTP:
                authenticationService = otpAuthenticationService;
                break;
                //            case PASSWORD:
                //                authenticationService = passwordAuthenticationServiceImpl;
                //                break;
            default:
                throw new InvalidAuthTypeException(
                        InternalErrorCode.INVALID_CONFIG, "Invalid Auth Type");
        }
        return authenticationService;
    }
}
