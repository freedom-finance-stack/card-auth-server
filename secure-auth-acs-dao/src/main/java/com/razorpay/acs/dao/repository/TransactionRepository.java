package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, String>{

}
