package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.RReqMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.DsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageLogService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ResultResponseValidator;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.InvalidStateTransactionException;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.StateMachine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

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
    public boolean processRreq(Transaction transaction) {
        boolean isSuccessful;
        try {
            handleRreq(transaction);
            isSuccessful = true;
        } catch (Exception ex) {
            isSuccessful = false;
            // Ignore any exception in sending RReq after retries and complete transaction
            log.error("An exception occurred: {} while sending RReq", ex.getMessage(), ex);
            try {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.RREQ_FAILED);
            } catch (InvalidStateTransactionException e) {
                log.error(
                        "An exception occurred: {} while making transition for RReq failed",
                        e.getMessage(),
                        e);
            }
        }
        return isSuccessful;
    }

    private void handleRreq(Transaction transaction)
            throws GatewayHttpStatusCodeException, InvalidStateTransactionException,
                    ValidationException {
        RREQ rreq = rReqMapper.toRreq(transaction);
        transactionMessageLogService.createAndSave(rreq, transaction.getId());
        try {
            RRES rres =
                    dsGatewayService.sendRReq(
                            Network.getNetwork(
                                    transaction.getTransactionCardDetail().getNetworkCode()),
                            rreq);
            transactionMessageLogService.createAndSave(rres, transaction.getId());
            resultResponseValidator.validateRequest(rres, rreq);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.RRES_RECEIVED);
        } catch (ValidationException e) {
            transaction.setTransactionStatus(e.getInternalErrorCode().getTransactionStatus());
            transaction.setErrorCode(InternalErrorCode.INVALID_RRES.getCode());
            sendDsErrorResponse(transaction, e.getThreeDSecureErrorCode(), e.getMessage());
            throw e;
        } catch (GatewayHttpStatusCodeException e) {
            transaction.setTransactionStatus(TransactionStatus.UNABLE_TO_AUTHENTICATE);
            if (e.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
                transaction.setErrorCode(InternalErrorCode.CONNECTION_TO_DS_FAILED.getCode());
                sendDsErrorResponse(
                        transaction, ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT, e.getMessage());
            } else {
                transaction.setErrorCode(InternalErrorCode.INVALID_RRES.getCode());
            }
            throw e;
        }
    }

    private void sendDsErrorResponse(
            Transaction transaction, ThreeDSecureErrorCode error, String errorDetails) {
        // send error message to DS.  Ignore all the exception as its 2nd communication to DS, we
        // want to send Cres back
        ThreeDSErrorResponse errorMessage =
                Util.generateErrorResponse(error, transaction, errorDetails);
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
