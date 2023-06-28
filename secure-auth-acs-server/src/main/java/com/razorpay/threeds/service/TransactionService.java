package com.razorpay.threeds.service;

import com.razorpay.acs.contract.AREQ;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;

public interface TransactionService {

  // save or update transaction in database and returns transaction with current hibernate session
  Transaction saveOrUpdate(Transaction transaction) throws ACSDataAccessException;
  // create transaction from areq
  Transaction create(AREQ areq);
  // find transaction by id in database
  Transaction findById(String id) throws ACSDataAccessException;
}
