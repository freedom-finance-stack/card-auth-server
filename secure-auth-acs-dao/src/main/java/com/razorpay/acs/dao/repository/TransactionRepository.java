package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.Transaction;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction, String> {
}
