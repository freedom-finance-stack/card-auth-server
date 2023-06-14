package com.razorpay.threeds.service;

import com.razorpay.acs.dao.model.Transaction;

public interface TransactionService {
    Transaction createOrUpdate(Transaction transactionDto);

    void remove(String id);

    Transaction findById(String id);
}
