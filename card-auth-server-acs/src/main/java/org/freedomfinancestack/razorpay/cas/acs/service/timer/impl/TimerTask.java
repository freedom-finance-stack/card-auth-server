package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimerTask implements Runnable {
    private final TransactionTimerService transactionTimerService;
    private final String transactionId;

    public TimerTask(String transactionId, TransactionTimerService transactionTimerService) {
        this.transactionTimerService = transactionTimerService;
        this.transactionId = transactionId;
    }

    @Override
    public void run() {
        log.info("Processing timer task for transaction id : {}", transactionId);
        transactionTimerService.performTask(transactionId);
        transactionTimerService.cancelTask(transactionId);
        log.info("Completed timer task for transaction id : {}", transactionId);
    }
}
