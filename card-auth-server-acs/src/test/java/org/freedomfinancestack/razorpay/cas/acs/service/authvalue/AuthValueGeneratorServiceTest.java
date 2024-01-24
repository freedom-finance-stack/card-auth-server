package org.freedomfinancestack.razorpay.cas.acs.service.authvalue;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl.MasterCardAuthValueGeneratorImpl;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl.VisaAuthValueGeneratorImpl;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthValueGeneratorServiceTest {

    @Mock private ApplicationContext applicationContext;

    @InjectMocks private AuthValueGeneratorService authValueGeneratorService;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
    }

    @Test
    void testGetAuthValue() throws ACSException, ThreeDSException {
        // Mock dependencies
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        VisaAuthValueGeneratorImpl visaAuthValueGenerator = mock(VisaAuthValueGeneratorImpl.class);
        when(applicationContext.getBean(VisaAuthValueGeneratorImpl.class))
                .thenReturn(visaAuthValueGenerator);
        when(visaAuthValueGenerator.createAuthValue(any())).thenReturn("fakeAuthValue");

        // Test the service method
        String authValue = authValueGeneratorService.getAuthValue(transaction);

        // Verify the result
        assertNotNull(authValue);
        assertEquals("fakeAuthValue", authValue);

        // Verify that the correct auth value generator was retrieved
        verify(applicationContext).getBean(VisaAuthValueGeneratorImpl.class);
    }

    @Test
    void testGetAuthValueMasterCard() throws ACSException, ThreeDSException {
        // Mock dependencies
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionCardDetail().setNetworkCode((byte) 2);
        MasterCardAuthValueGeneratorImpl authValueGenerator =
                mock(MasterCardAuthValueGeneratorImpl.class);
        when(applicationContext.getBean(MasterCardAuthValueGeneratorImpl.class))
                .thenReturn(authValueGenerator);
        when(authValueGenerator.createAuthValue(any())).thenReturn("fakeAuthValue");

        // Test the service method
        String authValue = authValueGeneratorService.getAuthValue(transaction);

        // Verify the result
        assertNotNull(authValue);
        assertEquals("fakeAuthValue", authValue);

        // Verify that the correct auth value generator was retrieved
        verify(applicationContext).getBean(MasterCardAuthValueGeneratorImpl.class);
    }

    @Test
    void testGetAuthValueNonPAMessageCategory() throws ACSException, ThreeDSException {
        // Test when the message category is not PA
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setMessageCategory(MessageCategory.NPA);
        String authValue = authValueGeneratorService.getAuthValue(transaction);
        assertNull(authValue);
    }

    @Test
    void testGetAuthValueMissingCardDetail() {
        // Test when transaction has no card detail
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionCardDetail(null);

        assertThrows(
                ACSValidationException.class,
                () -> authValueGeneratorService.getAuthValue(transaction),
                "Scheme not valid");
    }

    @Test
    void testGetAuthValueMissingNetworkCode() {
        // Test when card detail is missing network code
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionCardDetail().setNetworkCode(null);

        assertThrows(
                ACSValidationException.class,
                () -> authValueGeneratorService.getAuthValue(transaction),
                "Scheme not valid");
    }

    @Test
    void testGetAuthValueInvalidNetwork() {
        // Test when card detail has an invalid network code
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionCardDetail().setNetworkCode((byte) 4);

        assertThrows(
                ACSValidationException.class,
                () -> authValueGeneratorService.getAuthValue(transaction),
                "Scheme not valid");
    }
}
