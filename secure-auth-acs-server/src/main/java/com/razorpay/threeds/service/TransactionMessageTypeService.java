package com.razorpay.threeds.service;

import com.razorpay.acs.contract.ThreeDSObject;
import com.razorpay.acs.dao.model.TransactionMessageTypeDetail;

public interface TransactionMessageTypeService {
  void save(TransactionMessageTypeDetail transactionMessageTypeDetail);

  TransactionMessageTypeDetail createAndSave(ThreeDSObject threeDSObject, String transactionId);
}
