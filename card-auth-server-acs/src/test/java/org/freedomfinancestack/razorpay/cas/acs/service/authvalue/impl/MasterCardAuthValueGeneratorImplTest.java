package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import org.freedomfinancestack.extensions.crypto.NoOpEncryptionUtils;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AuthValueConfig;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MasterCardAuthValueGeneratorImplTest {
    @Mock private AuthValueConfig authValueConfig;

    @InjectMocks private MasterCardAuthValueGeneratorImpl authValueGenerator;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryptionUtils.INSTANCE);
    }

    @Test
    void createAuthValue_shouldGenerateAuthValue() throws ACSException {
        // arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        String mockAcsKey = "B039878C1F96D212F509B2DC4CC8CD1BB039878C1F96D212F509B2DC4CC8CD1B";

        when(authValueConfig.getMasterCardAcsKey()).thenReturn(mockAcsKey);
        // Act
        String result = authValueGenerator.createAuthValue(transaction);
        // Assert
        assertEquals("xgQEKSryAAAAAAAAAAAAAAAAAAAA", result);
    }

    @Test
    void createAuthValue_shouldHandleAcsValidationException() {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        String mockAcsKey = "mockAcsKey";
        when(authValueConfig.getMasterCardAcsKey()).thenReturn(mockAcsKey);
        // Act & Assert
        assertThrows(ACSException.class, () -> authValueGenerator.createAuthValue(transaction));
    }
}
