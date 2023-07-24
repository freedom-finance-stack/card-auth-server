package com.razorpay.ffs.cas.acs.service;

import com.razorpay.ffs.cas.contract.ThreeDSObject;
import com.razorpay.ffs.cas.dao.model.TransactionMessageTypeDetail;

public interface TransactionMessageTypeService {
    void save(TransactionMessageTypeDetail transactionMessageTypeDetail);

    TransactionMessageTypeDetail createAndSave(ThreeDSObject threeDSObject, String transactionId);
}
