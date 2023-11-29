package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import java.util.concurrent.TimeUnit;

import org.freedomfinancestack.extensions.scheduledTask.exception.TaskAlreadyExistException;
import org.freedomfinancestack.extensions.timer.TimerService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.generateTaskIdentifier;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.getIdFromTaskIdentifier;

@Service("aReqTransactionTimerService")
@RequiredArgsConstructor
@Slf4j
public class AReqTransactionTimerService implements TransactionTimerService {
    private final TimerService timerService;
    private final AppConfiguration appConfiguration;

    private final TransactionTimeOutService transactionTimeOutService;
    public static final String AREQ_TIMER_TASK_IDENTIFIER_KEY = "AREQ_TIMER_TASK";

    @Override
    public void scheduleTask(
            String transactionId, TransactionStatus transactionStatus, int decoupledTimeOut) {
        log.info("Scheduling timer task for transactionId: {}", transactionId);
        TimerTask task =
                new TimerTask(
                        generateTaskIdentifier(AREQ_TIMER_TASK_IDENTIFIER_KEY, transactionId),
                        this);
        try {
            int timeout = appConfiguration.getAcs().getTimeout().getChallengeRequest();
            if (transactionStatus.equals(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED)) {
                timeout =
                        decoupledTimeOut != 0
                                ? decoupledTimeOut
                                : appConfiguration
                                                .getAcs()
                                                .getTimeout()
                                                .getDecoupledChallengeCompletion()
                                        * 60;
            }
            timerService.scheduleTimeoutTask(
                    generateTaskIdentifier(AREQ_TIMER_TASK_IDENTIFIER_KEY, transactionId),
                    task,
                    timeout,
                    TimeUnit.SECONDS);
        } catch (TaskAlreadyExistException e) {
            log.error("Task already scheduled for transactionId: {}", transactionId);
        }
    }

    @Override
    public void cancelTask(String transactionId) {
        log.info("Removing AREQ timer task for transactionId: {}", transactionId);
        boolean isRemoved =
                timerService.removeTimeoutTask(
                        generateTaskIdentifier(AREQ_TIMER_TASK_IDENTIFIER_KEY, transactionId));
        if (!isRemoved) {
            log.info("Task not found for transactionId: {}", transactionId);
        }
        log.info("Task removed for transactionId: {}", transactionId);
    }

    @Override
    public void performTask(String timerTaskId) {
        log.info("AREQ Timer Task picked up for transactionId: {}", timerTaskId);
        try {
            transactionTimeOutService.performTimeOutWaitingForCreq(
                    getIdFromTaskIdentifier(AREQ_TIMER_TASK_IDENTIFIER_KEY, timerTaskId));
        } catch (Exception e) {
            log.error("Error while performing timer task waiting for challenge request", e);
        }

        log.info("AREQ Timer Task completed for transactionId: {}", timerTaskId);
    }
}
