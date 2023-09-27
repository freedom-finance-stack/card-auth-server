package org.freedomfinancestack.razorpay.cas.acs.service.timer;

public interface TransactionTimerService {
    void scheduleTask(String transactionId);

    void cancelTask(String transactionId);

    void performTask(String transactionId);
}
