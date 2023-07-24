package com.razorpay.acs.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.acs.dao.model.Transaction;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction, String> {}
