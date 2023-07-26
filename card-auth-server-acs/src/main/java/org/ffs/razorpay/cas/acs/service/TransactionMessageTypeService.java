package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.contract.ThreeDSObject;
import org.ffs.razorpay.cas.dao.model.TransactionMessageTypeDetail;

public interface TransactionMessageTypeService {
    void save(TransactionMessageTypeDetail transactionMessageTypeDetail);

    void createAndSave(ThreeDSObject threeDSObject, String transactionId);
}
