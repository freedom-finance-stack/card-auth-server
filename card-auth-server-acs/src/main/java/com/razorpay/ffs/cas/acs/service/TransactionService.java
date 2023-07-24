package com.razorpay.ffs.cas.acs.service;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.contract.AREQ;
import com.razorpay.ffs.cas.dao.model.Transaction;

public interface TransactionService {

    // save or update transaction in database and returns transaction with current hibernate session
    Transaction saveOrUpdate(Transaction transaction) throws ACSDataAccessException;
    // create transaction from areq
    Transaction create(AREQ areq) throws ValidationException;
    // find transaction by id in database
    Transaction findById(String id) throws ACSDataAccessException;
}
