package com.razorpay.ffs.cas.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.razorpay.ffs.cas.dao.model.CardDetail;

@Repository
public interface CardDetailRepository extends BaseRepository<CardDetail, String> {
    @Query("select r from CardDetail r where r.cardNumber = ?1")
    CardDetail findByCardNumber(String cardNumber);
}
