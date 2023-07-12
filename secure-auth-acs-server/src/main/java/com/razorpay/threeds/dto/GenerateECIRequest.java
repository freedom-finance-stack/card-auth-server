package com.razorpay.threeds.dto;

import com.razorpay.acs.contract.enums.MessageCategory;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.model.Network;

import lombok.Data;

@Data
public class GenerateECIRequest {
    // required
    TransactionStatus transactionStatus;
    Network network;
    MessageCategory messageCategory;
    // Optional
    String threeRIInd;

    public GenerateECIRequest(
            TransactionStatus transactionStatus, Network network, MessageCategory messageCategory) {
        this.transactionStatus = transactionStatus;
        this.network = network;
        this.messageCategory = messageCategory;
    }

    public GenerateECIRequest setThreeRIInd(String threeRIInd) {
        this.threeRIInd = threeRIInd;
        return this;
    }
}
