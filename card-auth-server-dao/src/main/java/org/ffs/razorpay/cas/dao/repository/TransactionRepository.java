package org.ffs.razorpay.cas.dao.repository;

import org.ffs.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction, String> {}
