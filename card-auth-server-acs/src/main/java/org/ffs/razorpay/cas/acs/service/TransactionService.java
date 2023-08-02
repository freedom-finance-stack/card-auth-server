package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.dao.model.Transaction;

public interface TransactionService {

    // save or update transaction in database and returns transaction with current hibernate session
    Transaction saveOrUpdate(Transaction transaction) throws ACSDataAccessException;
    // create transaction from areq
    Transaction create(AREQ areq) throws ValidationException;
    // find transaction by id in database
    Transaction findById(String id) throws ACSDataAccessException;
}
