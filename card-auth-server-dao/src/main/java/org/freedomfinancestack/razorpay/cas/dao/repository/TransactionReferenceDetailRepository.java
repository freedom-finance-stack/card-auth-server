package org.freedomfinancestack.razorpay.cas.dao.repository;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.dao.model.TransactionReferenceDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionReferenceDetailRepository
        extends BaseRepository<TransactionReferenceDetail, String> {

    List<TransactionReferenceDetail> findByThreedsServerTransactionId(
            String threedsServerTransactionId);
}
