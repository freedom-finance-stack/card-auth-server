package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.RReqMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ds.DsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ResultResponseValidator;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
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
    private final TransactionMessageTypeService transactionMessageTypeService;
    private final ResultResponseValidator resultResponseValidator;

    public ResultRequestServiceImpl(
            RReqMapper rReqMapper,
            @Qualifier("gatewayService") DsGatewayService dsGatewayService,
            TransactionMessageTypeService transactionMessageTypeService,
            ResultResponseValidator resultResponseValidator) {
        this.rReqMapper = rReqMapper;
        this.dsGatewayService = dsGatewayService;
        this.transactionMessageTypeService = transactionMessageTypeService;
        this.resultResponseValidator = resultResponseValidator;
    }

    @Override
    public void processRreq(Transaction transaction) {
        try {
            handleRreq(transaction);
        } catch (Exception ex) {
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
    }

    private void handleRreq(Transaction transaction)
            throws GatewayHttpStatusCodeException, InvalidStateTransactionException,
                    ValidationException {
        RREQ rreq = rReqMapper.toRreq(transaction);
        transactionMessageTypeService.createAndSave(rreq, transaction.getId());
        try {
            RRES rres =
                    dsGatewayService.sendRReq(
                            Network.getNetwork(
                                    transaction.getTransactionCardDetail().getNetworkCode()),
                            rreq);
            resultResponseValidator.validateRequest(rres, rreq);
            transactionMessageTypeService.createAndSave(rres, transaction.getId());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.RRES_RECEIVED);
        } catch (ValidationException e) {
            generateSendErrorResponse(transaction, e.getThreeDSecureErrorCode(), e.getMessage());
            throw e;
        } catch (GatewayHttpStatusCodeException e) {
            if (e.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
                generateSendErrorResponse(
                        transaction, ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT, e.getMessage());
            }
            throw e;
        }
    }

    private void generateSendErrorResponse(
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
