package org.ffs.razorpay.cas.acs.dto;

import org.ffs.razorpay.cas.contract.enums.MessageCategory;
import org.ffs.razorpay.cas.dao.enums.TransactionStatus;
import org.ffs.razorpay.cas.dao.model.Network;

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
