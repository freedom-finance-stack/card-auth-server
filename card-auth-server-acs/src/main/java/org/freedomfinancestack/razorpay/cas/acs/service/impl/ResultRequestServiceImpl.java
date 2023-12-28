package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.RReqMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DSConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ds.DsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageLogService;
import org.freedomfinancestack.razorpay.cas.acs.validation.ResultResponseValidator;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ResultRequestService}, which is responsible for to send Result Requests
 * to the DS.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Slf4j
public class ResultRequestServiceImpl implements ResultRequestService {
    private final RReqMapper rReqMapper;
    private final DsGatewayService dsGatewayService;
    private final TransactionMessageLogService transactionMessageLogService;
    private final ResultResponseValidator resultResponseValidator;

    public ResultRequestServiceImpl(
            RReqMapper rReqMapper,
            @Qualifier("gatewayService") DsGatewayService dsGatewayService,
            TransactionMessageLogService transactionMessageLogService,
            ResultResponseValidator resultResponseValidator) {
        this.rReqMapper = rReqMapper;
        this.dsGatewayService = dsGatewayService;
        this.transactionMessageLogService = transactionMessageLogService;
        this.resultResponseValidator = resultResponseValidator;
    }

    @Override
    public void handleRreq(Transaction transaction)
            throws GatewayHttpStatusCodeException,
                    InvalidStateTransactionException,
                    ACSValidationException,
                    DSConnectionException {
        RREQ rreq = rReqMapper.toRreq(transaction);
        transactionMessageLogService.createAndSave(rreq, transaction.getId());
        boolean success = false;
        // TODO extend ThreeDSErrorResponse from ThreeDSObject to hande validations properly
        RRES rres = null;
        try {
            rres =
                    dsGatewayService.sendRReq(
                            Network.getNetwork(
                                    transaction.getTransactionCardDetail().getNetworkCode()),
                            rreq);
            transactionMessageLogService.createAndSave(rres, transaction.getId());
            resultResponseValidator.validateRequest(rres, rreq);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.RRES_RECEIVED);
            success = true;
        } catch (ACSValidationException e) {
            transaction.setTransactionStatus(e.getInternalErrorCode().getTransactionStatus());
            transaction.setErrorCode(InternalErrorCode.INVALID_RRES.getCode());
            if (rres != null
                    && rres.getMessageType() != null
                    && rres.getMessageType().equals(MessageType.Erro.toString())) {
                throw new ACSValidationException(
                        ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, e.getMessage());
            }
            sendDsErrorResponse(transaction, e.getThreeDSecureErrorCode(), e.getMessage());
            throw e;
        } catch (GatewayHttpStatusCodeException e) {
            transaction.setTransactionStatus(TransactionStatus.UNABLE_TO_AUTHENTICATE);
            if (e.getHttpStatus().is4xxClientError()) {
                transaction.setErrorCode(InternalErrorCode.CONNECTION_TO_DS_FAILED.getCode());
                sendDsErrorResponse(
                        transaction, ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT, e.getMessage());
            } else if (e.getHttpStatus().isSameCodeAs(HttpStatus.GATEWAY_TIMEOUT)) {
                transaction.setErrorCode(
                        InternalErrorCode.TRANSACTION_TIMED_OUT_DS_RESPONSE.getCode());
                sendDsErrorResponse(
                        transaction, ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT, e.getMessage());
                throw new DSConnectionException(
                        ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT,
                        InternalErrorCode.TRANSACTION_TIMED_OUT_DS_RESPONSE,
                        e.getMessage());
            } else {
                transaction.setErrorCode(InternalErrorCode.INVALID_RRES.getCode());
            }
            throw e;
        } finally {
            if (!success) {
                try {
                    transaction.setTransactionStatus(TransactionStatus.FAILED);
                    StateMachine.Trigger(transaction, Phase.PhaseEvent.RREQ_FAILED);
                } catch (InvalidStateTransactionException e) {
                    log.error(
                            "An exception occurred: {} while making transition for RReq failed",
                            e.getMessage(),
                            e);
                }
            }
        }
    }

    private void sendDsErrorResponse(
            Transaction transaction, ThreeDSecureErrorCode error, String errorDetails) {
        // send error message to DS.  Ignore all the exception as its 2nd communication to DS, we
        // want to send Cres back
        ThreeDSErrorResponse errorMessage =
                new ThreeDSException(error, errorDetails, transaction).getErrorResponse();
        try {
            dsGatewayService.sendError(
                    Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode()),
                    errorMessage);
        } catch (Exception ex) {
            log.error(
                    "An exception occurred: {} while sending error message in response to invalid"
                            + " rreq",
                    ex.getMessage(),
                    ex);
        }
    }
}
