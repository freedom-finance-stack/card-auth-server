package com.razorpay.ffs.cas.acs.service.authvalue;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.exception.ThreeDSException;
import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSException;
import com.razorpay.ffs.cas.acs.service.authvalue.impl.MasterCardAuthValueGeneratorImpl;
import com.razorpay.ffs.cas.acs.service.authvalue.impl.VisaAuthValueGeneratorImpl;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;
import com.razorpay.ffs.cas.dao.enums.Network;
import com.razorpay.ffs.cas.dao.model.Transaction;

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
