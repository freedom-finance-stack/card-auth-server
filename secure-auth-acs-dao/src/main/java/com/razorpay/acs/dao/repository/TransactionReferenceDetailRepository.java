package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.TransactionMessageTypeDetail;
import com.razorpay.acs.dao.model.TransactionReferenceDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface TransactionReferenceDetailRepository extends BaseRepository<TransactionReferenceDetail, String>{

    List<TransactionReferenceDetail> findByThreedsServerTransactionId(String threedsServerTransactionId);
}
