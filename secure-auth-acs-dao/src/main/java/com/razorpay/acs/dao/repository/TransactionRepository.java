package com.razorpay.acs.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.acs.dao.model.Transaction;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction, String> {
    // todo handle soft delete of all the child table entries
}
