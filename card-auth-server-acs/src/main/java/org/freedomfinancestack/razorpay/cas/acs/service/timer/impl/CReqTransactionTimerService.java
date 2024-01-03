package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import java.util.concurrent.TimeUnit;

import io.micrometer.tracing.Tracer;
import org.freedomfinancestack.extensions.scheduledTask.exception.TaskAlreadyExistException;
import org.freedomfinancestack.extensions.timer.TimerService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.generateTaskIdentifier;

@Service("cReqTransactionTimerService")
@RequiredArgsConstructor
@Slf4j
public class CReqTransactionTimerService implements TransactionTimerService {
    private final TimerService timerService;
    private final AReqTransactionTimerService aReqTransactionTimeoutService;
    private final AppConfiguration appConfiguration;
    private final TransactionTimeOutService transactionTimeOutService;
    private final Tracer tracer;
    public static String CREQ_TIMER_TASK_IDENTIFIER_KEY = "CREQ_TIMER_TASK";

    @Override
    public void scheduleTask(
            String transactionId, TransactionStatus transactionStatus, String decoupledTimer) {
        log.info("Scheduling timer task for transactionId: {}", transactionId);
        // deleting Areq task if exist
        aReqTransactionTimeoutService.cancelTask(
                generateTaskIdentifier(AREQ_TIMER_TASK_IDENTIFIER_KEY, (transactionId)));

        TimerTask task =
                new TimerTask(
                        generateTaskIdentifier(CREQ_TIMER_TASK_IDENTIFIER_KEY, transactionId),
                        this, tracer);
        try {
            timerService.scheduleTimeoutTask(
                    task.getTimerTaskId(),
                    task,
                    appConfiguration.getAcs().getTimeout().getChallengeCompletion(),
                    TimeUnit.SECONDS);
        } catch (TaskAlreadyExistException e) {
            log.error("Task already scheduled for transactionId: {}", transactionId);
        }
        log.info("Timer task scheduled for transactionId: {}", transactionId);
    }

    @Override
    public void cancelTask(String transactionId) {
        boolean isRemoved =
                timerService.removeTimeoutTask(
                        generateTaskIdentifier(CREQ_TIMER_TASK_IDENTIFIER_KEY, transactionId));
        if (!isRemoved) {
            log.info("Task not found for transactionId: {}", transactionId);
        }
        log.info("Task removed for transactionId: {}", transactionId);
    }

    @Override
    public void performTask(String timerTaskId) {
        try {
            transactionTimeOutService.performTimeOutWaitingForChallengeCompletion(
                    getIdFromTaskIdentifier(CREQ_TIMER_TASK_IDENTIFIER_KEY, timerTaskId));
        } catch (Exception e) {
            log.error("Error while performing timer task waiting for challenge completion", e);
        }
    }
}
