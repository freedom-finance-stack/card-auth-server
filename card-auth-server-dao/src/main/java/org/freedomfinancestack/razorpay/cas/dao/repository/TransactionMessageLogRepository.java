package org.freedomfinancestack.razorpay.cas.dao.repository;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageLog;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMessageLogRepository
        extends BaseRepository<TransactionMessageLog, String> {
    List<TransactionMessageLog> findAllByTransactionId(String transactionId);
}
