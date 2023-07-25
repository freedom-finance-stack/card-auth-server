package com.razorpay.ffs.cas.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.ffs.cas.dao.model.Transaction;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction, String> {}
