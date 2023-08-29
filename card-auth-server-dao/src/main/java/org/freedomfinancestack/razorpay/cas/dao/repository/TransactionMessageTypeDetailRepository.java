package org.freedomfinancestack.razorpay.cas.dao.repository;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageTypeDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMessageTypeDetailRepository
        extends BaseRepository<TransactionMessageTypeDetail, String> {
    List<TransactionMessageTypeDetail> findAllByTransactionId(String transactionId);
}
