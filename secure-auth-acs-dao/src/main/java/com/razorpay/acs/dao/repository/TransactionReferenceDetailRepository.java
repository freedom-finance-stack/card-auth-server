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
    @Override
    @Modifying
    @Transactional
    @Query("update TransactionReferenceDetail t set t.deleted_at= now() where t.id = ?1") // define query to delete all
    void softDeleteById(String id);

    List<TransactionReferenceDetail> findByThreedsServerTransactionId(String threedsServerTransactionId);
}
