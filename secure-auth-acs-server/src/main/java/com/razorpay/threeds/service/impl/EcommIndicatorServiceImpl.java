package com.razorpay.threeds.service.impl;

import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.contract.enums.ThreeRIInd;
import com.razorpay.acs.dao.enums.ECI;
import com.razorpay.acs.dao.enums.Network;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.threeds.dto.GenerateECIRequest;
import com.razorpay.threeds.service.ECommIndicatorService;

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
      if (ThreeRIInd.RECURRING_TRANSACTION.getValue().equals(generateECIRequest.getThreeRIInd())) {
        return ECI.MC_SUCCESS_RPA.getValue();
      } else {
        return ECI.getValue(transactionStatus, network, generateECIRequest.getMessageCategory());
      }

    } else {
      return ECI.getValue(transactionStatus, network);
    }
  }
}
