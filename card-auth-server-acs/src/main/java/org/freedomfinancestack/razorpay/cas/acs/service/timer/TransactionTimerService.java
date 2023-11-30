package org.freedomfinancestack.razorpay.cas.acs.service.timer;

import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

public interface TransactionTimerService {
    void scheduleTask(
            String transactionId, TransactionStatus transactionStatus, String decoupledTimeOut);

    void cancelTask(String transactionId);

    void performTask(String timerTaskId);
}
