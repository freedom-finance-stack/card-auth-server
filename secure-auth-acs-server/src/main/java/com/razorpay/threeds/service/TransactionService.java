package com.razorpay.threeds.service;

import com.razorpay.threeds.dto.TransactionDto;

public interface TransactionService {
    TransactionDto create(TransactionDto transactionDto);

    void remove(String id);

    void removeAll();

    TransactionDto findById(String id);
}
