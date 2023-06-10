package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.Transaction;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository  extends BaseRepository<Transaction, String>{
}
