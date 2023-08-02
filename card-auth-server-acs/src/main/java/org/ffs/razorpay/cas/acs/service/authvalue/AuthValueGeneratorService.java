package org.ffs.razorpay.cas.acs.service.authvalue;

import java.util.Objects;

import org.ffs.razorpay.cas.acs.exception.acs.ACSException;
import org.ffs.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;
import org.ffs.razorpay.cas.acs.service.authvalue.impl.MasterCardAuthValueGeneratorImpl;
import org.ffs.razorpay.cas.acs.service.authvalue.impl.VisaAuthValueGeneratorImpl;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.ffs.razorpay.cas.dao.enums.Network;
import org.ffs.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "authValueGeneratorService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthValueGeneratorService {

    private final ApplicationContext applicationContext;

    public String getAuthValue(@NonNull final Transaction transaction)
            throws ACSException, ThreeDSException {
        if (transaction.getTransactionCardDetail() == null
                || transaction.getTransactionCardDetail().getNetworkCode() == null) {
            log.error(
                    "getAuthValueGenerator() Error occurred while fetching correct auth value"
                            + " generator");
            throw new ValidationException(
                    ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID, "Scheme not valid");
        }

        AuthValueGenerator authValueGenerator =
                getAuthValueGenerator(
                        Objects.requireNonNull(
                                Network.getNetwork(
                                        transaction
                                                .getTransactionCardDetail()
                                                .getNetworkCode()
                                                .intValue())));

        if (authValueGenerator == null) {
            log.error("getAuthValue() Error occurred as auth value generator is empty or null");
            throw new ValidationException(
                    ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID, "Scheme not valid");
        }
        return authValueGenerator.createAuthValue(transaction);
    }

    /**
     * Factory Method to fetch correct Auth Value generator corresponding to network
     *
     * @param network which is {@link com.razorpay.acs.dao.model.Network}
     * @return {@link AuthValueGenerator}
     */
    private AuthValueGenerator getAuthValueGenerator(@NonNull final Network network) {

        switch (network) {
            case VISA:
                return applicationContext.getBean(VisaAuthValueGeneratorImpl.class);
            case MASTERCARD:
                return applicationContext.getBean(MasterCardAuthValueGeneratorImpl.class);
        }
        return null;
    }
}
