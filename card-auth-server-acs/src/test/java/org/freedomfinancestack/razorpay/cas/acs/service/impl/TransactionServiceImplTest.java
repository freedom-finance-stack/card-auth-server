package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.text.ParseException;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.dao.enums.ChallengeCancelIndicator;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData.createSampleAREQ;
import static org.freedomfinancestack.razorpay.cas.contract.utils.Util.DATE_FORMAT_YYYYMMDDHHMMSS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    @Mock private TransactionRepository transactionRepository;

    @Mock private ECommIndicatorService eCommIndicatorService;

    @InjectMocks private TransactionServiceImpl transactionService;

    @Test
    public void testSaveOrUpdate() throws ACSDataAccessException {
        // Mock data
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        // Mock behavior

        when(transactionRepository.findById(anyString())).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        // Test
        Transaction result = transactionService.saveOrUpdate(transaction);

        // Verify
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionRepository, times(1)).findById(transaction.getId());
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
    }

    @Test
    public void testSaveOrUpdateException() {
        // Mock data
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        // Mock behavior to simulate an exception
        when(transactionRepository.save(any(Transaction.class)))
                .thenThrow(new DataAccessException("Simulated error") {});

        // Test
        assertThrows(
                ACSDataAccessException.class, () -> transactionService.saveOrUpdate(transaction));

        // Verify
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testFindById() throws ACSDataAccessException, TransactionDataNotValidException {
        // Mock data
        Transaction sampleTransaction = TransactionTestData.createSampleBrwTransaction();
        String transactionId = sampleTransaction.getId();
        sampleTransaction.setId(transactionId);

        // Mock behavior for findById
        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(sampleTransaction));

        // Test
        Transaction result = transactionService.findById(transactionId);

        // Verify
        verify(transactionRepository, times(1)).findById(transactionId);
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        // Mock data
        String transactionId = "nonExistentTransactionId";

        // Mock behavior for findById
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // Test
        assertThrows(
                TransactionDataNotValidException.class,
                () -> {
                    transactionService.findById(transactionId);
                });

        // Verify
        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    public void testCreateTransaction() throws ACSValidationException, ParseException {
        // Mock data
        AREQ sampleAreq = AREQTestData.createSampleAREQ();

        // Test
        Transaction result = transactionService.create(sampleAreq);

        // Verify
        assertNotNull(result);

        // Verify createTransactionFromAreq method
        assertEquals(sampleAreq.getTransactionId(), result.getId());
        assertEquals(Phase.AREQ, result.getPhase());
        assertEquals(TransactionStatus.CREATED, result.getTransactionStatus());

        // Verify buildTransactionCardDetail method
        assertEquals(sampleAreq.getAcctNumber(), result.getTransactionCardDetail().getCardNumber());
        assertEquals(
                sampleAreq.getCardExpiryDate(), result.getTransactionCardDetail().getCardExpiry());
        assertEquals(
                sampleAreq.getCardholderName(),
                result.getTransactionCardDetail().getCardholderName());

        // Verify buildTransactionBrowserDetail method
        assertEquals(
                sampleAreq.getBrowserAcceptHeader(),
                result.getTransactionBrowserDetail().getAcceptHeader());
        assertEquals(sampleAreq.getBrowserIP(), result.getTransactionBrowserDetail().getIp());

        // Verify buildTransactionSDKDetail method
        assertEquals(sampleAreq.getSdkAppID(), result.getTransactionSdkDetail().getSdkAppID());
        assertEquals(sampleAreq.getSdkTransID(), result.getTransactionSdkDetail().getSdkTransId());
        assertEquals(
                sampleAreq.getSdkReferenceNumber(),
                result.getTransactionSdkDetail().getSdkReferenceNumber());
        assertEquals(sampleAreq.getDeviceInfo(), result.getTransactionSdkDetail().getDeviceInfo());

        // Verify buildTransactionReferenceDetail method
        assertEquals(
                sampleAreq.getThreeDSServerTransID(),
                result.getTransactionReferenceDetail().getThreedsServerTransactionId());
        assertEquals(
                sampleAreq.getThreeDSServerRefNumber(),
                result.getTransactionReferenceDetail().getThreedsServerReferenceNumber());
        assertEquals(
                sampleAreq.getDsTransID(),
                result.getTransactionReferenceDetail().getDsTransactionId());
        assertEquals(sampleAreq.getDsURL(), result.getTransactionReferenceDetail().getDsUrl());
        assertEquals(
                sampleAreq.getNotificationURL(),
                result.getTransactionReferenceDetail().getNotificationUrl());
        assertEquals(
                sampleAreq.getThreeDSRequestorChallengeInd(),
                result.getTransactionReferenceDetail().getThreeDSRequestorChallengeInd());

        // Verify buildTransactionMerchant method
        assertEquals(
                sampleAreq.getAcquirerMerchantID(),
                result.getTransactionMerchant().getAcquirerMerchantId());
        assertEquals(
                sampleAreq.getMerchantName(), result.getTransactionMerchant().getMerchantName());
        assertEquals(
                Short.valueOf(sampleAreq.getMerchantCountryCode()),
                result.getTransactionMerchant().getMerchantCountryCode());

        // Verify buildTransactionPurchaseDetail method
        assertEquals(
                sampleAreq.getPurchaseAmount(),
                result.getTransactionPurchaseDetail().getPurchaseAmount());
        assertEquals(
                sampleAreq.getPurchaseCurrency(),
                result.getTransactionPurchaseDetail().getPurchaseCurrency());
        assertEquals(
                Util.getTimeStampFromString(
                        sampleAreq.getPurchaseDate(), DATE_FORMAT_YYYYMMDDHHMMSS),
                result.getTransactionPurchaseDetail().getPurchaseTimestamp());
        assertEquals(
                Byte.valueOf(sampleAreq.getPurchaseExponent()),
                result.getTransactionPurchaseDetail().getPurchaseExponent());
        assertEquals(
                Boolean.valueOf(sampleAreq.getPayTokenInd()),
                result.getTransactionPurchaseDetail().getPayTokenInd());
    }

    @Test
    public void testCreateTransactionWithInvalidDate() {
        // Mock data with an invalid date
        AREQ sampleAREQ = createSampleAREQ();
        sampleAREQ.setPurchaseDate("invalid_date");
        // Test
        assertThrows(ACSValidationException.class, () -> transactionService.create(sampleAREQ));
    }

    @Test
    public void testUpdateTransactionWithError() {
        // Mock data
        Transaction sampleTransaction = TransactionTestData.createSampleBrwTransaction();
        InternalErrorCode internalErrorCode = InternalErrorCode.TRANSACTION_SAVE_EXCEPTION;

        // Call the method
        transactionService.updateTransactionWithError(internalErrorCode, sampleTransaction);

        // Assertions
        assertEquals(
                ChallengeCancelIndicator.TRANSACTION_ERROR.getIndicator(),
                sampleTransaction.getChallengeCancelInd());
        assertEquals(internalErrorCode.getCode(), sampleTransaction.getErrorCode());
        assertEquals(
                internalErrorCode.getTransactionStatus(), sampleTransaction.getTransactionStatus());
        assertEquals(
                internalErrorCode.getTransactionStatusReason().getCode(),
                sampleTransaction.getTransactionStatusReason());
    }

    @Test
    public void testUpdateEci() {
        // Mock data
        Transaction sampleTransaction = TransactionTestData.createSampleAppTransaction();

        // Mock the eCommIndicatorService
        when(eCommIndicatorService.generateECI(any(GenerateECIRequest.class)))
                .thenReturn("mocked_eci");

        // Call the method
        transactionService.updateEci(sampleTransaction);

        // Assertions
        assertEquals("mocked_eci", sampleTransaction.getEci());
        verify(eCommIndicatorService, times(1)).generateECI(any(GenerateECIRequest.class));
    }

    @Test
    public void testSaveOrUpdate_WithNullTransaction() {
        assertThrows(
                ACSDataAccessException.class,
                () -> transactionService.saveOrUpdate(null),
                "saveOrUpdate should throw ACSDataAccessException for null transaction");
    }

    @Test
    public void testSaveOrUpdate_WithSaveException() {
        // Mock data
        Transaction sampleTransaction = TransactionTestData.createSampleBrwTransaction();

        // Mock the transactionRepository
        doThrow(new DataAccessException("Mocked save exception") {})
                .when(transactionRepository)
                .save(any(Transaction.class));

        // Call the method
        assertThrows(
                ACSDataAccessException.class,
                () -> transactionService.saveOrUpdate(sampleTransaction),
                "saveOrUpdate should throw ACSDataAccessException for save exception");

        // Verify that findById is not called when there's a save exception
        verify(transactionRepository, never()).findById(anyString());
    }

    @Test
    public void testFindById_WithBlankId() {
        assertThrows(
                TransactionDataNotValidException.class,
                () -> transactionService.findById(""),
                "findById should throw TransactionDataNotValidException for blank ID");
    }

    @Test
    public void testFindById_WithNullId() {
        assertThrows(
                TransactionDataNotValidException.class,
                () -> transactionService.findById(null),
                "findById should throw TransactionDataNotValidException for null ID");
    }

    @Test
    public void testFindById_WithEmptyResult() {
        // Mock the transactionRepository
        when(transactionRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call the method
        assertThrows(
                TransactionDataNotValidException.class,
                () -> transactionService.findById("nonexistentId"),
                "findById should throw TransactionDataNotValidException for empty result");
    }

    @Test
    public void testCreate_WithInvalidPurchaseDate() {
        // Mock data
        AREQ invalidPurchaseDateAreq = AREQTestData.createSampleAREQ();
        invalidPurchaseDateAreq.setPurchaseDate("invalidDate");

        // Call the method
        assertThrows(
                ACSValidationException.class,
                () -> transactionService.create(invalidPurchaseDateAreq),
                "create should throw ACSValidationException for invalid PurchaseDate");
    }
}
