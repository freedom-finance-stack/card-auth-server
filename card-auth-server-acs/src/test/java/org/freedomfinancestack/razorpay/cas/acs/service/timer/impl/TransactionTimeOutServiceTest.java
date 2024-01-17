package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.extensions.crypto.NoOpEncryptionUtils;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.threedsrequestor.ThreedsRequestorCResService;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionTimeOutServiceTest {

    @Mock private TransactionService transactionService;

    @Mock private ResultRequestService resultRequestService;

    @Mock private ThreedsRequestorCResService threedsRequestorCResService;

    @InjectMocks private TransactionTimeOutService timeoutService;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryptionUtils.INSTANCE);
    }

    @Test
    void testPerformTimeOutWaitingForCreq() throws Exception {
        // Arrange
        String transactionId = "sampleTransactionId";
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.ARES);
        transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        when(transactionService.findById(transactionId)).thenReturn(transaction);

        // Act
        timeoutService.performTimeOutWaitingForCreq(transactionId);

        // Assert
        verify(resultRequestService, times(1)).handleRreq(any());
        verify(transactionService, times(1)).updateTransactionWithError(any(), any());
        verify(transactionService, times(1)).updateEci(any());
        verify(transactionService, times(1)).saveOrUpdate(any());
    }

    @Test
    void testPerformTimeOutWaitingForCreqWithDecoupledTransaction() throws Exception {
        // Arrange
        String transactionId = "decoupledTransactionId";
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.ARES);
        transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED);
        when(transactionService.findById(transactionId)).thenReturn(transaction);

        // Act
        timeoutService.performTimeOutWaitingForCreq(transactionId);

        // Assert
        verify(resultRequestService, times(1)).handleRreq(any());
        verify(transactionService, times(1)).updateTransactionWithError(any(), any());
        verify(transactionService, times(1)).updateEci(any());
        verify(transactionService, times(1)).saveOrUpdate(any());
    }

    @Test
    void testPerformTimeOutWaitingForChallengeCompletion() throws Exception {
        // Arrange
        String transactionId = "sampleTransactionId";
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.ARES);
        when(transactionService.findById(transactionId)).thenReturn(transaction);

        // Act
        timeoutService.performTimeOutWaitingForChallengeCompletion(transactionId);

        // Assert
        verify(resultRequestService, times(1)).handleRreq(any());
        verify(transactionService, times(1)).updateTransactionWithError(any(), any());
        verify(transactionService, times(1)).updateEci(any());
        verify(transactionService, times(1)).saveOrUpdate(any());
    }

    @Test
    void testPerformTimeOutWaitingForCreqWithInvalidStateTransactionException() throws Exception {
        // Arrange
        String transactionId = "invalidStateTransactionId";
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.AREQ);
        transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        when(transactionService.findById(transactionId)).thenReturn(transaction);

        // Act and Assert
        timeoutService.performTimeOutWaitingForCreq(transactionId);
    }

    @Test
    void testPerformTimeOutWaitingForCreqWithACSDataAccessException() throws Exception {
        // Arrange
        String transactionId = "acsDataAccessExceptionTransactionId";
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setPhase(Phase.ARES);
        transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        when(transactionService.findById(transactionId)).thenThrow(ACSDataAccessException.class);

        // Act and Assert
        timeoutService.performTimeOutWaitingForCreq(transactionId);
    }

    // Add more test cases for different scenarios, timeouts, and exception cases
}
