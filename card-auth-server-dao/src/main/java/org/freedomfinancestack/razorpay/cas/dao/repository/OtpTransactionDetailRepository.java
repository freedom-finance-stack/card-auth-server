package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.enums.OtpVerificationStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpTransactionDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpTransactionDetailRepository
        extends BaseRepository<OtpTransactionDetail, String> {
    OtpTransactionDetail findByTransactionId(String transactionId);

    OtpTransactionDetail findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
            String transactionId, OtpVerificationStatus verificationStatus);
}
