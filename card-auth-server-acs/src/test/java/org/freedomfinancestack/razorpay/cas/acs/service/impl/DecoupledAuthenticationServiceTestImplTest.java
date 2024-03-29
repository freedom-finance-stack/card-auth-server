package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrqService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrs;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DecoupledAuthenticationServiceTestImplTest {

    @Mock private POrqService porqService;

    @InjectMocks private DecoupledAuthenticationServiceTestImpl decoupledAuthenticationService;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
    }

    @Test
    void testProcessAuthenticationRequest_Successful() throws ThreeDSException {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        DecoupledAuthenticationRequest decoupledAuthenticationRequest =
                new DecoupledAuthenticationRequest();

        // Mock behavior
        when(porqService.sendPOrq(any(), any(), any())).thenReturn(getpOrs(true));

        // Act
        DecoupledAuthenticationResponse response =
                decoupledAuthenticationService.processAuthenticationRequest(
                        transaction, decoupledAuthenticationRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccessful());

        // Verify interactions with mocks
        verify(porqService, times(1))
                .sendPOrq(
                        eq(transaction.getId()),
                        eq(
                                transaction
                                        .getTransactionReferenceDetail()
                                        .getThreedsServerTransactionId()),
                        eq(transaction.getMessageVersion()));
    }

    private static POrs getpOrs(boolean p_isOobSuccessful) {
        return new POrs("POrs", p_isOobSuccessful, "2.2.0", "1.2.2");
    }

    @Test
    void testProcessAuthenticationRequest_Unsuccessful() throws ThreeDSException {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        DecoupledAuthenticationRequest decoupledAuthenticationRequest =
                new DecoupledAuthenticationRequest();

        // Mock behavior
        when(porqService.sendPOrq(any(), any(), any())).thenReturn(getpOrs(false));

        // Act
        DecoupledAuthenticationResponse response =
                decoupledAuthenticationService.processAuthenticationRequest(
                        transaction, decoupledAuthenticationRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccessful());

        // Verify interactions with mocks
        verify(porqService, times(1))
                .sendPOrq(
                        eq(transaction.getId()),
                        eq(
                                transaction
                                        .getTransactionReferenceDetail()
                                        .getThreedsServerTransactionId()),
                        eq(transaction.getMessageVersion()));
    }

    @Test
    void testProcessAuthenticationRequest_Null() throws ThreeDSException {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        DecoupledAuthenticationRequest decoupledAuthenticationRequest =
                new DecoupledAuthenticationRequest();

        // Mock behavior
        when(porqService.sendPOrq(any(), any(), any())).thenReturn(null);

        // Act
        DecoupledAuthenticationResponse response =
                decoupledAuthenticationService.processAuthenticationRequest(
                        transaction, decoupledAuthenticationRequest);

        // Assert
        assertNull(response);

        // Verify interactions with mocks
        verify(porqService, times(1))
                .sendPOrq(
                        eq(transaction.getId()),
                        eq(
                                transaction
                                        .getTransactionReferenceDetail()
                                        .getThreedsServerTransactionId()),
                        eq(transaction.getMessageVersion()));
    }
}
