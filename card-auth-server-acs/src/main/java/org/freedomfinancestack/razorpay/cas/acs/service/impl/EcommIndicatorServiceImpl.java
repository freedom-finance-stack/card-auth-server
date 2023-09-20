package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeRIInd;
import org.freedomfinancestack.razorpay.cas.dao.enums.ECI;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.springframework.stereotype.Service;

/**
 * The {@code EcommIndicatorServiceImpl} class is an implementation of the {@link
 * ECommIndicatorService} interface. This service is responsible for generating the E-commerce
 * Indicator (ECI) value based on the provided {@link GenerateECIRequest}. The ECI value indicates
 * the outcome of an authentication request and is used in 3D Secure transactions to convey the
 * result of the authentication.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class EcommIndicatorServiceImpl implements ECommIndicatorService {

    /**
     * Generates the E-commerce Indicator (ECI) value based on the provided {@link
     * GenerateECIRequest}.
     *
     * @param generateECIRequest The {@link GenerateECIRequest} object containing the details
     *     required to generate the ECI value.
     * @return The E-commerce Indicator (ECI) value as a String.
     */
    @Override
    public String generateECI(GenerateECIRequest generateECIRequest) {
        Network network = Network.getNetwork(generateECIRequest.getNetworkCode());
        TransactionStatus transactionStatus = generateECIRequest.getTransactionStatus();
        if (generateECIRequest.getTransactionStatus() == TransactionStatus.CREATED) {
            transactionStatus = TransactionStatus.SUCCESS;
        }
        if (Network.MASTERCARD == network) {
            if (ThreeRIInd.RECURRING_TRANSACTION
                    .getValue()
                    .equals(generateECIRequest.getThreeRIInd())) {
                return ECI.MC_SUCCESS_RPA.getValue();
            } else {
                return ECI.getValue(
                        transactionStatus, network, generateECIRequest.getMessageCategory());
            }

        } else {
            return ECI.getValue(transactionStatus, network);
        }
    }
}
