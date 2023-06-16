package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.CardRange;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface CardRangeRepository extends BaseRepository<CardRange, String>{

    @Query("select r from CardRange r where r.startRange < ?1 and r.endRange > ?1")
    CardRange findByPan(Long pan);
}
