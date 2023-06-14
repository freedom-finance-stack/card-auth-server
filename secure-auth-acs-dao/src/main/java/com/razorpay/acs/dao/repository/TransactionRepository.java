package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface TransactionRepository  extends BaseRepository<Transaction, String>{
    @Override
    @Modifying
    @Transactional
    @Query("update Transaction t set t.deleted_at= now() where t.id = ?1") // define query to delete all
    void softDeleteById(String id);
}
