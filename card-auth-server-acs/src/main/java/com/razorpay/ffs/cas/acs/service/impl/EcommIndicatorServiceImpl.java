package com.razorpay.ffs.cas.acs.service.impl;

import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.dto.GenerateECIRequest;
import com.razorpay.ffs.cas.acs.service.ECommIndicatorService;
import com.razorpay.ffs.cas.contract.enums.ThreeRIInd;
import com.razorpay.ffs.cas.dao.enums.ECI;
import com.razorpay.ffs.cas.dao.enums.Network;
import com.razorpay.ffs.cas.dao.enums.TransactionStatus;

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
