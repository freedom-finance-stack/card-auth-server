package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.dao.enums.ChallengeCancelIndicator;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionTimeOutService {

    private final TransactionService transactionService;
    private final ECommIndicatorService eCommIndicatorService;
    private final ResultRequestService resultRequestService;

    void performTimeOutWaitingForCreq(String transactionId) {
        try {
            // todo add mutex
            Transaction transaction = transactionService.findById(transactionId);
            // if CREQ not recived
            if ((transaction.getTransactionStatus() == TransactionStatus.CHALLENGE_REQUIRED
                            || transaction.getTransactionStatus()
                                    == TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED)
                    && (transaction.getPhase() == Phase.ARES
                            || transaction.getPhase() == Phase.AREQ)) {
                log.info("Timeout : CREQ not received for transactionId: {}", transactionId);
                timeOutTransaction(
                        transaction,
                        ChallengeCancelIndicator
                                .TRANSACTION_TIMED_OUT_AT_ACS_FIRST_CREQ_NOT_RECEIVED_BY_ACS,
                        InternalErrorCode.TRANSACTION_TIMED_OUT_WAITING_FOR_CREQ);
            }

        } catch (ACSDataAccessException | InvalidStateTransactionException ex) {
            log.error(
                    " Error while performing timer task before creq transactionId: {} ",
                    transactionId,
                    ex);
        }
    }

    void performTimeOutWaitingForChallengeCompletion(String transactionId) {
        // todo get mutex
        try {
            Transaction transaction = transactionService.findById(transactionId);
            log.info(
                    "transaction status"
                            + transaction.getTransactionStatus()
                            + " challenge completed : "
                            + Util.isChallengeCompleted(transaction));
            if (!Util.isChallengeCompleted(transaction)) {
                log.info("Timeout : Challenge not completed for transactionId: {}", transactionId);
                timeOutTransaction(
                        transaction,
                        ChallengeCancelIndicator.TRANSACTION_TIMED_OUT,
                        InternalErrorCode.TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION);
            } else {
                log.info("release mutex");
                // todo release mutex
            }
        } catch (ACSDataAccessException | InvalidStateTransactionException e) {
            log.error(
                    " Error while performing timer task waiting for challenge completion"
                            + " transactionId: {} ",
                    transactionId,
                    e);
        }
    }

    private void timeOutTransaction(
            Transaction transaction,
            ChallengeCancelIndicator challengeCancelIndicator,
            InternalErrorCode errorCode)
            throws ACSDataAccessException, InvalidStateTransactionException {
        transaction.setChallengeCancelInd(challengeCancelIndicator.getIndicator());
        Util.updateTransaction(transaction, errorCode);
        updateEci(transaction);
        StateMachine.Trigger(transaction, Phase.PhaseEvent.TIMEOUT);
        transaction = transactionService.saveOrUpdate(transaction);
        // todo release mutex before RReq.
        resultRequestService.processRreq(transaction);
    }

    private void updateEci(Transaction transaction) {
        GenerateECIRequest generateECIRequest =
                new GenerateECIRequest(
                        transaction.getTransactionStatus(),
                        transaction.getTransactionCardDetail().getNetworkCode(),
                        transaction.getMessageCategory());
        generateECIRequest.setThreeRIInd(transaction.getThreeRIInd());
        String eci = eCommIndicatorService.generateECI(generateECIRequest);
        transaction.setEci(eci);
    }
}
