package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.CardDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardDetailRepository extends BaseRepository<CardDetail, String> {
    @Query("select r from CardDetail r where r.cardNumber = ?1")
    CardDetail findByCardNumber(String cardNumber);

    @Modifying
    @Transactional
    @Query(
            "update CardDetail r set r.blocked = true where r.cardNumber = ?1 and r.institutionId"
                    + " = ?2")
    void blockCard(String cardNumber, String institutionId);
}
