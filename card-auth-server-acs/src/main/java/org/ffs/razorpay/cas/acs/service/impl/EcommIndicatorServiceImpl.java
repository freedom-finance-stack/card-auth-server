package org.ffs.razorpay.cas.acs.service.impl;

import org.ffs.razorpay.cas.acs.dto.GenerateECIRequest;
import org.ffs.razorpay.cas.acs.service.ECommIndicatorService;
import org.ffs.razorpay.cas.contract.enums.ThreeRIInd;
import org.ffs.razorpay.cas.dao.enums.ECI;
import org.ffs.razorpay.cas.dao.enums.Network;
import org.ffs.razorpay.cas.dao.enums.TransactionStatus;
import org.springframework.stereotype.Service;

@Service
public class EcommIndicatorServiceImpl implements ECommIndicatorService {

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
