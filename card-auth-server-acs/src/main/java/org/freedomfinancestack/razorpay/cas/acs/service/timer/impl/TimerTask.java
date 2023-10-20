package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TimerTask implements Runnable {
    private final TransactionTimerService transactionTimerService;
    private final String timerTaskId;

    public TimerTask(String timerTaskId, TransactionTimerService transactionTimerService) {
        this.transactionTimerService = transactionTimerService;
        this.timerTaskId = timerTaskId;
    }

    @Override
    public void run() {
        log.info("Processing timer task for transaction id : {}", timerTaskId);
        transactionTimerService.performTask(timerTaskId);
        log.info("Completed timer task for transaction id : {}", timerTaskId);
    }
}
