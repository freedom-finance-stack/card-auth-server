package org.freedomfinancestack.razorpay.cas.acs.utils;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.SignerServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {
    @Test
    public void getPublicKey_SuccessCase() throws SignerServiceException {
        KeyPair keyPair = SecurityUtil.generateEphermalKeyPair();

        assertNotNull(keyPair);
        assertInstanceOf(ECPublicKey.class, keyPair.getPublic());
        assertInstanceOf(ECPrivateKey.class, keyPair.getPrivate());
    }

    @Test
    public void getPublicKey_NullPointerException() {
        // Arrange
        KeyPair keyPair = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> SecurityUtil.getPublicKey(keyPair));
    }

    @Test
    public void getPublicKey_ClassCase_ExceptionCase() {
        // Arrange
        KeyPair keyPair = mock(KeyPair.class);
        PublicKey publicKey = mock(PublicKey.class);
        when(keyPair.getPublic()).thenReturn(publicKey);

        // Act and Assert
        assertThrows(ClassCastException.class, () -> SecurityUtil.getPublicKey(keyPair));
    }
}
