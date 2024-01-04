package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TimerTask implements Runnable {
    private final TransactionTimerService transactionTimerService;
    private final String timerTaskId;
    private final Tracer tracer;

    public TimerTask(
            String timerTaskId, TransactionTimerService transactionTimerService, Tracer tracer) {
        this.transactionTimerService = transactionTimerService;
        this.timerTaskId = timerTaskId;
        this.tracer = tracer;
    }

    @Override
    public void run() {
        Span newSpan = tracer.nextSpan().name("performTask").start();
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan.start())) {
            log.info("Processing timer task for transaction id : {}", timerTaskId);
            transactionTimerService.performTask(timerTaskId);
            log.info("Completed timer task for transaction id : {}", timerTaskId);
        } finally {
            newSpan.end();
        }
    }
}
