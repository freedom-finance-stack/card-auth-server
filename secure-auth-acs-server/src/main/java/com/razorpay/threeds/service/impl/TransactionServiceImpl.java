package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.acs.dao.repository.TransactionRepository;
import com.razorpay.threeds.dto.TransactionDto;
import com.razorpay.threeds.dto.mapper.TransactionMapper;
import com.razorpay.threeds.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
//@ComponentScan(basePackages = "com.razorpay.threeds.dao.repository")
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionDto create(TransactionDto transactionDto) {
        transactionRepository.save(TransactionMapper.INSTANCE.toTransactionModel(transactionDto));
        return transactionDto;
    }

    public void remove(String id) {
        transactionRepository.deleteById(id);
    }

    public void removeAll() {
        transactionRepository.deleteAll();
    }

    public TransactionDto findById(String id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return TransactionMapper.INSTANCE.toTransactionDto(transaction.get());
        }
        return null;
    }

}
