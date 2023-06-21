package com.razorpay.threeds.service;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.model.Transaction;

public interface TransactionService {
  Transaction createOrUpdate(Transaction transaction);

  Transaction create(AREQ areq);

  Transaction save(Transaction transaction);

  Transaction findById(String id);

  Transaction findDuplicationTransaction(String threedsServerTransactionId);
}
