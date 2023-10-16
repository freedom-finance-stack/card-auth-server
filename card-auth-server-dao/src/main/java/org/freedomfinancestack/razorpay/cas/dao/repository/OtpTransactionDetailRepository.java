package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.NotificationDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpTransactionDetailRepository
        extends BaseRepository<NotificationDetail, String> {}
