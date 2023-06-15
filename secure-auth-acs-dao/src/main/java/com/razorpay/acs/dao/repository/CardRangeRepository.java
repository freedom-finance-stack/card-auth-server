package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.CardRange;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface CardRangeRepository extends BaseRepository<CardRange, String>{
    @Override
    @Modifying
    @Transactional
    @Query("update CardRange t set t.deleted_at= now() where t.id = ?1") // define query to delete all
    void softDeleteById(String id);

    @Query("select r from CardRange r where r.startRange < ?1 and r.endRange > ?1")
    CardRange findByPan(Long pan);
}
