package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.acs.dao.model.TransactionMessageTypeDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface TransactionMessageTypeDetailRepository extends BaseRepository<TransactionMessageTypeDetail, String>{
}
