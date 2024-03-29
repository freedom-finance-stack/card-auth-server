package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.freedomfinancestack.extensions.scheduledTask.exception.TaskAlreadyExistException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.extensions.timer.TimerService;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.DecoupledAuthenticationService;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.generateTaskIdentifier;

@Service("decoupledAuthenticationAsyncService")
@RequiredArgsConstructor
@Slf4j
public class DecoupledAuthenticationAsyncService implements TransactionTimerService {
    private final TimerService timerService;
    private final AppConfiguration appConfiguration;
    private final DecoupledAuthenticationService decoupledAuthenticationService;
    private final TransactionService transactionService;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final ResultRequestService resultRequestService;
    private final ECommIndicatorService eCommIndicatorService;
    private final Tracer tracer;
    private static final String[] DA_TIMEOUT_TEST_RANGE = new String[] {"R_TEST_1"};
    public static String DECOUPLED_AUTH_TIMER_TASK_IDENTIFIER_KEY = "DECOUPLED_AUTH_TIMER_TASK";

    @Override
    public void scheduleTask(
            String transactionId, TransactionStatus transactionStatus, String decoupledTimer) {
        log.info("Scheduling timer task for transactionId: {}", transactionId);
        if (TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED.equals(transactionStatus)) {
            TimerTask task =
                    new TimerTask(
                            generateTaskIdentifier(
                                    DECOUPLED_AUTH_TIMER_TASK_IDENTIFIER_KEY, transactionId),
                            this,
                            tracer);
            try {
                timerService.scheduleTimeoutTask(
                        task.getTimerTaskId(),
                        task,
                        appConfiguration.getAcs().getTimeout().getDecoupledAuthDelay(),
                        TimeUnit.SECONDS);
            } catch (TaskAlreadyExistException e) {
                log.error("Task already scheduled for transactionId: {}", transactionId);
            }
            log.info("Timer task scheduled for transactionId: {}", transactionId);
        }
    }

    @Override
    public void cancelTask(String transactionId) {
        boolean isRemoved =
                timerService.removeTimeoutTask(
                        generateTaskIdentifier(
                                DECOUPLED_AUTH_TIMER_TASK_IDENTIFIER_KEY, transactionId));
        if (!isRemoved) {
            log.info("Task not found for transactionId: {}", transactionId);
        }
        log.info("Task removed for transactionId: {}", transactionId);
    }

    @Override
    public void performTask(String timerTaskId) {
        try {
            String transactionId =
                    getIdFromTaskIdentifier(DECOUPLED_AUTH_TIMER_TASK_IDENTIFIER_KEY, timerTaskId);

            Transaction transaction = transactionService.findById(transactionId);

            DecoupledAuthenticationResponse response =
                    decoupledAuthenticationService.processAuthenticationRequest(
                            transaction, new DecoupledAuthenticationRequest());

            if (Arrays.stream(DA_TIMEOUT_TEST_RANGE)
                    .anyMatch(testRange -> transaction.getCardRangeId().equals(testRange))) {
                return;
            }

            transaction.setTransactionStatus(
                    response == null || !response.isSuccessful()
                            ? TransactionStatus.FAILED
                            : TransactionStatus.SUCCESS);

            String eci =
                    eCommIndicatorService.generateECI(
                            new GenerateECIRequest(
                                            transaction.getTransactionStatus(),
                                            transaction.getTransactionCardDetail().getNetworkCode(),
                                            transaction.getMessageCategory())
                                    .setThreeRIInd(transaction.getThreeRIInd()));
            transaction.setEci(eci);

            if (transaction.getTransactionStatus().equals(TransactionStatus.SUCCESS)) {
                transaction.setAuthValue(authValueGeneratorService.getAuthValue(transaction));
            } else {
                transaction.setTransactionStatusReason(
                        TransactionStatusReason.CARD_AUTHENTICATION_FAILED.getCode());
            }

            try {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.DECOUPLED_AUTH_COMPLETED);
                resultRequestService.handleRreq(transaction);
            } catch (Exception ex) {
                log.error("An exception occurred: {} while sending RReq", ex.getMessage(), ex);
            }
            transactionService.saveOrUpdate(transaction);

        } catch (Exception e) {
            log.error("Error while performing timer task waiting for challenge completion", e);
        }
    }
}
