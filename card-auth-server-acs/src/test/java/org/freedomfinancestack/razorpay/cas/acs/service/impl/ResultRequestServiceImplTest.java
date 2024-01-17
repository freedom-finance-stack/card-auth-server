package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.crypto.NoOpEncryptionUtils;
import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.razorpay.cas.acs.data.RREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.RReqMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DSConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ds.DsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageLogService;
import org.freedomfinancestack.razorpay.cas.acs.validation.ResultResponseValidator;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultRequestServiceImplTest {

    @Mock private RReqMapper rReqMapper;

    @Mock
    @Qualifier("gatewayService") private DsGatewayService dsGatewayService;

    @Mock private TransactionMessageLogService transactionMessageLogService;

    @Mock private ResultResponseValidator resultResponseValidator;

    @InjectMocks private ResultRequestServiceImpl resultRequestService;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryptionUtils.INSTANCE);
    }

    @Test
    void testHandleRreq_Successful() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);
        RREQ rreq = RREQTestData.getValidRReq();
        RRES rres = RREQTestData.getValidRRes();

        when(rReqMapper.toRreq(transaction)).thenReturn(rreq);
        when(dsGatewayService.sendRReq(any(), any())).thenReturn(rres);
        doNothing().when(transactionMessageLogService).createAndSave(any(), any());
        doNothing().when(resultResponseValidator).validateRequest(any(), any());

        // Act
        resultRequestService.handleRreq(transaction);
        assertEquals(Phase.CRES, transaction.getPhase());

        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(2)).createAndSave(any(), any());
        verify(resultResponseValidator, times(1)).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_ACSValidationException() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);

        when(rReqMapper.toRreq(transaction)).thenReturn(mock(RREQ.class));
        when(dsGatewayService.sendRReq(any(), any()))
                .thenThrow(
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "error"));
        doNothing().when(dsGatewayService).sendError(any(), any());

        // Act/Assert
        assertThrows(
                ACSValidationException.class, () -> resultRequestService.handleRreq(transaction));

        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(InternalErrorCode.INVALID_RRES.getCode(), transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());

        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(dsGatewayService, times(1)).sendError(any(), any());
        verify(transactionMessageLogService, times(1)).createAndSave(any(), any());
        verify(resultResponseValidator, never()).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_ValidateRequestACSValidationException() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);
        RREQ rreq = RREQTestData.getValidRReq();
        RRES rres = RREQTestData.getValidRRes();

        when(rReqMapper.toRreq(transaction)).thenReturn(rreq);
        when(dsGatewayService.sendRReq(any(), any())).thenReturn(rres);
        doNothing().when(dsGatewayService).sendError(any(), any());
        doNothing().when(transactionMessageLogService).createAndSave(any(), any());
        doThrow(
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Validation error"))
                .when(resultResponseValidator)
                .validateRequest(any(), any());

        // Act/Assert
        ACSValidationException ex =
                assertThrows(
                        ACSValidationException.class,
                        () -> resultRequestService.handleRreq(transaction));

        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(InternalErrorCode.INVALID_RRES.getCode(), transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());
        assertEquals(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, ex.getThreeDSecureErrorCode());
        assertEquals(InternalErrorCode.INVALID_REQUEST, ex.getInternalErrorCode());
        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendError(any(), any());
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(2)).createAndSave(any(), any());
        verify(resultResponseValidator, times(1)).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_GatewayHttpStatusCodeException4xx() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);

        when(rReqMapper.toRreq(transaction)).thenReturn(mock(RREQ.class));
        when(dsGatewayService.sendRReq(any(), any()))
                .thenThrow(
                        new GatewayHttpStatusCodeException(HttpStatus.BAD_REQUEST, "Bad request"));
        doNothing().when(dsGatewayService).sendError(any(), any());
        // Act/Assert
        GatewayHttpStatusCodeException ex =
                assertThrows(
                        GatewayHttpStatusCodeException.class,
                        () -> resultRequestService.handleRreq(transaction));

        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(
                InternalErrorCode.CONNECTION_TO_DS_FAILED.getCode(), transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendError(any(), any());
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(1)).createAndSave(any(), any());
        verify(resultResponseValidator, never()).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_GatewayHttpStatusCodeExceptionTimeout() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);

        when(rReqMapper.toRreq(transaction)).thenReturn(mock(RREQ.class));
        when(dsGatewayService.sendRReq(any(), any()))
                .thenThrow(
                        new GatewayHttpStatusCodeException(
                                HttpStatus.GATEWAY_TIMEOUT, "Gateway timeout"));
        doNothing().when(dsGatewayService).sendError(any(), any());

        // Act/Assert
        DSConnectionException ex =
                assertThrows(
                        DSConnectionException.class,
                        () -> resultRequestService.handleRreq(transaction));

        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(
                InternalErrorCode.TRANSACTION_TIMED_OUT_DS_RESPONSE.getCode(),
                transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());
        assertEquals(ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT, ex.getThreeDSecureErrorCode());
        assertEquals(
                InternalErrorCode.TRANSACTION_TIMED_OUT_DS_RESPONSE, ex.getInternalErrorCode());
        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendError(any(), any());
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(1)).createAndSave(any(), any());
        verify(resultResponseValidator, never()).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_GatewayHttpStatusCode5XXTimeout() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);

        when(rReqMapper.toRreq(transaction)).thenReturn(mock(RREQ.class));
        when(dsGatewayService.sendRReq(any(), any()))
                .thenThrow(
                        new GatewayHttpStatusCodeException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Gateway timeout"));

        // Act/Assert
        GatewayHttpStatusCodeException ex =
                assertThrows(
                        GatewayHttpStatusCodeException.class,
                        () -> resultRequestService.handleRreq(transaction));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());

        // verify transaction data
        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(InternalErrorCode.INVALID_RRES.getCode(), transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());

        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(1)).createAndSave(any(), any());
        verify(resultResponseValidator, never()).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_GetErroFromDs() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);
        RREQ rreq = RREQTestData.getValidRReq();
        RRES rres = RREQTestData.getValidRRes();
        rres.setMessageType(MessageType.Erro.toString());

        when(rReqMapper.toRreq(transaction)).thenReturn(rreq);
        when(dsGatewayService.sendRReq(any(), any())).thenReturn(rres);

        doNothing().when(transactionMessageLogService).createAndSave(any(), any());
        doThrow(
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Validation error"))
                .when(resultResponseValidator)
                .validateRequest(any(), any());

        // Act
        ACSValidationException ex =
                assertThrows(
                        ACSValidationException.class,
                        () -> resultRequestService.handleRreq(transaction));

        // verify transaction data
        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(InternalErrorCode.INVALID_RRES.getCode(), transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());

        assertEquals(ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ex.getThreeDSecureErrorCode());
        assertEquals(InternalErrorCode.INVALID_REQUEST, ex.getInternalErrorCode());

        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(2)).createAndSave(any(), any());
        verify(resultResponseValidator, times(1)).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_ErrorInSendingErroMessage() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.RREQ);

        when(rReqMapper.toRreq(transaction)).thenReturn(mock(RREQ.class));
        when(dsGatewayService.sendRReq(any(), any()))
                .thenThrow(
                        new GatewayHttpStatusCodeException(
                                HttpStatus.GATEWAY_TIMEOUT, "Gateway timeout"));
        doThrow(new RuntimeException("Gateway error"))
                .when(dsGatewayService)
                .sendError(any(), any());

        // Act/Assert
        DSConnectionException ex =
                assertThrows(
                        DSConnectionException.class,
                        () -> resultRequestService.handleRreq(transaction));

        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(
                InternalErrorCode.TRANSACTION_TIMED_OUT_DS_RESPONSE.getCode(),
                transaction.getErrorCode());
        assertEquals(Phase.RREQ, transaction.getPhase());
        assertEquals(ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT, ex.getThreeDSecureErrorCode());
        assertEquals(
                InternalErrorCode.TRANSACTION_TIMED_OUT_DS_RESPONSE, ex.getInternalErrorCode());
        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendError(any(), any());
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(1)).createAndSave(any(), any());
        verify(resultResponseValidator, never()).validateRequest(any(), any());
    }

    @Test
    void testHandleRreq_PhaseTransitionError() throws Exception {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.CREQ);
        RREQ rreq = RREQTestData.getValidRReq();
        RRES rres = RREQTestData.getValidRRes();

        when(rReqMapper.toRreq(transaction)).thenReturn(rreq);
        when(dsGatewayService.sendRReq(any(), any())).thenReturn(rres);
        doNothing().when(transactionMessageLogService).createAndSave(any(), any());
        doNothing().when(resultResponseValidator).validateRequest(any(), any());

        // Act
        assertThrows(
                InvalidStateTransactionException.class,
                () -> resultRequestService.handleRreq(transaction));

        assertEquals(Phase.CREQ, transaction.getPhase());
        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        // Verify interactions with mocks
        verify(rReqMapper, times(1)).toRreq(transaction);
        verify(dsGatewayService, times(1)).sendRReq(any(), any());
        verify(transactionMessageLogService, times(2)).createAndSave(any(), any());
        verify(resultResponseValidator, times(1)).validateRequest(any(), any());
    }
}
