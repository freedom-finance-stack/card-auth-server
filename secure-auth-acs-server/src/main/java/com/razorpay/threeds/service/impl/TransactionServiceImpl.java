package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.acs.dao.repository.TransactionRepository;
import com.razorpay.threeds.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction createOrUpdate(Transaction transaction) {
        Transaction transactionModel = transactionRepository.save(transaction);
        return transactionModel;
    }

    public void remove(String id) {
        transactionRepository.softDeleteById(id);
    }


    public Transaction findById(String id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return transaction.get();
        }
        return null;
    }

}
