package com.razorpay.acs.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.razorpay.acs.dao.model.CardDetail;

@Repository
public interface CardDetailRepository extends BaseRepository<CardDetail, String> {
  @Query("select r from CardDetail r where r.cardNumber = ?1")
  CardDetail findByCardNumber(String cardNumber);
}
