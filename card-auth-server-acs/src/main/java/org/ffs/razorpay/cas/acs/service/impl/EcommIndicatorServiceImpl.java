package org.ffs.razorpay.cas.acs.service.impl;

import org.ffs.razorpay.cas.acs.dto.GenerateECIRequest;
import org.ffs.razorpay.cas.acs.service.ECommIndicatorService;
import org.ffs.razorpay.cas.contract.enums.ThreeRIInd;
import org.ffs.razorpay.cas.dao.enums.ECI;
import org.ffs.razorpay.cas.dao.enums.Network;
import org.ffs.razorpay.cas.dao.enums.TransactionStatus;
import org.springframework.stereotype.Service;

/**
 * The {@code EcommIndicatorServiceImpl} class is an implementation of the {@link
 * ECommIndicatorService} interface. This service is responsible for generating the E-commerce
 * Indicator (ECI) value based on the provided {@link GenerateECIRequest}. The ECI value indicates
 * the outcome of an authentication request and is used in 3D Secure transactions to convey the
 * result of the authentication.
 *
 * @version 1.0.0
 * @since ACS 1.0.0
 * @author jaydeepRadadiya
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

        TransactionStatus transactionStatus = generateECIRequest.getTransactionStatus();
        Network network = generateECIRequest.getNetwork().getName();
        if (generateECIRequest.getTransactionStatus() == TransactionStatus.CREATED) {
            transactionStatus = TransactionStatus.SUCCESS;
        }
        if (Network.MASTERCARD == generateECIRequest.getNetwork().getName()) {
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
