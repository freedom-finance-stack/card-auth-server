package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import java.util.concurrent.TimeUnit;

import org.freedomfinancestack.extensions.scheduledTask.exception.TaskAlreadyExistException;
import org.freedomfinancestack.extensions.timer.TimerService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("aReqTransactionTimerService")
@RequiredArgsConstructor
@Slf4j
public class AReqTransactionTimerService implements TransactionTimerService {
    private final TimerService timerService;
    private final AppConfiguration appConfiguration;
    private final TransactionTimeOutService transactionTimeOutService;

    @Override
    public void scheduleTask(String transactionId) {
        log.info("Scheduling timer task for transactionId: {}", transactionId);
        TimerTask task = new TimerTask(getAreqTaskIdentifier(transactionId), this);
        try {
            timerService.scheduleTimeoutTask(
                    getAreqTaskIdentifier(transactionId),
                    task,
                    appConfiguration.getAcs().getTimeout().getChallengeCompletion(),
                    TimeUnit.SECONDS);
        } catch (TaskAlreadyExistException e) {
            log.error("Task already scheduled for transactionId: {}", transactionId);
        }
    }

    @Override
    public void cancelTask(String transactionId) {
        boolean isRemoved = timerService.removeTimeoutTask(getAreqTaskIdentifier(transactionId));
        if (!isRemoved) {
            log.info("Task not found for transactionId: {}", transactionId);
        }
        log.info("Task removed for transactionId: {}", transactionId);
    }

    @Override
    public void performTask(String transactionId) {
        transactionTimeOutService.performTimeOutWatingForCreq(transactionId);
    }

    public static String getAreqTaskIdentifier(String transactionId) {
        return "AREQ[" + transactionId + "]";
    }
}
