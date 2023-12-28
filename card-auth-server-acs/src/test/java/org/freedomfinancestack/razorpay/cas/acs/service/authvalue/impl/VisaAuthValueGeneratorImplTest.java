package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.CVVGenerationService;
import org.freedomfinancestack.razorpay.cas.dao.enums.ECI;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisaAuthValueGeneratorImplTest {

    @Mock
    private CVVGenerationService cvvGenerationService;

    @InjectMocks
    private VisaAuthValueGeneratorImpl visaAuthValueGenerator;

    @ParameterizedTest
    @CsvSource({  "SUCCESS",  "UNABLE_TO_AUTHENTICATE",  "FAILED", "INFORMATIONAL", "REJECTED"})
    void createAuthValue_SuccessfulFlow( String status) throws ACSValidationException, ACSException {
        // Arrange
        TransactionStatus transactionStatus = TransactionStatus.valueOf(status);
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(transactionStatus);
        transaction.setEci(ECI.VISA_SUCCESS.getValue());
        when(cvvGenerationService.generateCVV(any(), any())).thenReturn("123");

        // Act
        String authValue = visaAuthValueGenerator.createAuthValue(transaction);

        // Assert
        assertNotNull(authValue);
    }

    @Test
    void createAuthValue_SuccessfulFlow_ChallengeManadatedTrue( ) throws ACSValidationException, ACSException {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setEci(ECI.VISA_SUCCESS.getValue());
        when(cvvGenerationService.generateCVV(any(), any())).thenReturn("123");

        // Act
        String authValue = visaAuthValueGenerator.createAuthValue(transaction);

        // Assert
        assertNotNull(authValue);
    }

    @Test
    void createAuthValue_ExceptionInCVVGeneration() throws  ACSException {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setEci(ECI.VISA_SUCCESS.getValue());
        when(cvvGenerationService.generateCVV(any(), any())).thenThrow(new ACSException(InternalErrorCode.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(ACSException.class, () -> visaAuthValueGenerator.createAuthValue(transaction));
    }

    @Test
    void createAuthValue_NullTransactionStatus() {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        transaction.setEci(ECI.VISA_SUCCESS.getValue());
        // Act & Assert
        assertThrows(ACSValidationException.class, () -> visaAuthValueGenerator.createAuthValue(transaction));
    }

    // Add more test cases to cover different scenarios


}
