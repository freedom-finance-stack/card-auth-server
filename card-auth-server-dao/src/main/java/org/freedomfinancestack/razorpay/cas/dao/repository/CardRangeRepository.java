package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRangeRepository extends BaseRepository<CardRange, String> {

    @Query("select r from CardRange r where r.startRange < ?1 and r.endRange > ?1")
    CardRange findByPan(Long pan);
}
