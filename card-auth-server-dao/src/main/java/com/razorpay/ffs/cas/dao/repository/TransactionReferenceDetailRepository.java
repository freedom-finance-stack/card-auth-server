package com.razorpay.ffs.cas.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.razorpay.ffs.cas.dao.model.TransactionReferenceDetail;

@Repository
public interface TransactionReferenceDetailRepository
        extends BaseRepository<TransactionReferenceDetail, String> {

    List<TransactionReferenceDetail> findByThreedsServerTransactionId(
            String threedsServerTransactionId);
}
