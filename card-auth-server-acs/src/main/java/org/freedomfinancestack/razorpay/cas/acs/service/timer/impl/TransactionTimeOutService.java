package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ds.DsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.CResService;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
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
    private final DsGatewayService dsGatewayService;
    private final CResService cResService;

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

                if (transaction.getTransactionStatus()
                        == TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED) {
                    timeOutTransaction(
                            transaction,
                            ChallengeCancelIndicator.TRANSACTION_TIMED_OUT_DECOUPLED_AUTH,
                            InternalErrorCode.TRANSACTION_TIMED_OUT_DECOUPLED_AUTH);
                } else {
                    timeOutTransaction(
                            transaction,
                            ChallengeCancelIndicator
                                    .TRANSACTION_TIMED_OUT_AT_ACS_FIRST_CREQ_NOT_RECEIVED_BY_ACS,
                            InternalErrorCode.TRANSACTION_TIMED_OUT_WAITING_FOR_CREQ);
                }
            }

        } catch (ACSDataAccessException
                | InvalidStateTransactionException
                | TransactionDataNotValidException ex) {

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
            if (!ChallengeRequestService.isChallengeCompleted(transaction)) {
                log.info("Timeout : Challenge not completed for transactionId: {}", transactionId);
                timeOutTransaction(
                        transaction,
                        ChallengeCancelIndicator.TRANSACTION_TIMED_OUT,
                        InternalErrorCode.TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION);
            } else {
                // todo release mutex
            }
        } catch (ACSDataAccessException
                | InvalidStateTransactionException
                | TransactionDataNotValidException e) {
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
        transactionService.updateTransactionWithError(errorCode, transaction);
        StateMachine.Trigger(transaction, Phase.PhaseEvent.TIMEOUT);
        transactionService.updateEci(transaction);
        transactionService.saveOrUpdate(transaction);
        // todo release mutex before RReq.
        try {
            if (transaction.getDeviceChannel().equals(DeviceChannel.BRW.getChannel())
                    && challengeCancelIndicator
                            .getIndicator()
                            .equals(
                                    ChallengeCancelIndicator.TRANSACTION_TIMED_OUT
                                            .getIndicator())) {
                cResService.sendCRes(transaction);
            }
            resultRequestService.handleRreq(transaction);

        } catch (Exception ex) {
            log.error("An exception occurred: {} while sending RReq", ex.getMessage(), ex);
        } finally {
            transactionService.saveOrUpdate(transaction);
        }
    }
}
