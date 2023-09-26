package org.freedomfinancestack.razorpay.cas.acs.service;

import java.math.BigDecimal;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.model.ChallengeAuthTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code FeatureService} interface is responsible for fetching the authentication configuration
 * for a given set of card range, ground and institution ids. If config exist under multiple ids
 * provided it will give precedence in following order CardRange, CardRangeGroup, Institution.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 */
public interface FeatureService {
    AuthConfigDto getAuthenticationConfig(Map<FeatureEntityType, String> entityIdsByType)
            throws ACSDataAccessException;

    @Component
    @Slf4j
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    class AuthenticationServiceLocator {
        private final @Qualifier("OTPAuthenticationService") AuthenticationService
                otpAuthenticationService;

        public AuthenticationService locateTransactionAuthenticationService(
                Transaction transaction, ChallengeAuthTypeConfig authConfig)
                throws OperationNotSupportedException {
            String purchaseAmount = null;
            if (transaction.getTransactionPurchaseDetail() != null
                    && !Util.isNullorBlank(
                            transaction.getTransactionPurchaseDetail().getPurchaseAmount())) {
                purchaseAmount = transaction.getTransactionPurchaseDetail().getPurchaseAmount();
            }
            if (authConfig.getThresholdAuthType() != null
                    && !Util.isNullorBlank(purchaseAmount)
                    && new BigDecimal(purchaseAmount).compareTo(authConfig.getThreshold()) >= 0) {
                return locateService(authConfig.getThresholdAuthType());
            } else {
                return locateService(authConfig.getDefaultAuthType());
            }
        }

        public AuthenticationService locateService(AuthType authType)
                throws OperationNotSupportedException {
            AuthenticationService authenticationService = null;

            switch (authType) {
                case OTP:
                    authenticationService = otpAuthenticationService;
                    break;
                    //            case PASSWORD:
                    //                authenticationService = passwordAuthenticationServiceImpl;
                    //                break;
                default:
                    throw new OperationNotSupportedException(
                            InternalErrorCode.INVALID_CONFIG, "Invalid Auth Type");
            }
            return authenticationService;
        }
    }
}
