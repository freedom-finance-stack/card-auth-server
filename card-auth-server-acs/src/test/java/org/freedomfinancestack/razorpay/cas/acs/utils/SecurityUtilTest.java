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
    public void test_generate_valid_EC_key_pair() throws SignerServiceException {
        KeyPair keyPair = SecurityUtil.generateEphermalKeyPair();

        assertNotNull(keyPair);
        assertInstanceOf(ECPublicKey.class, keyPair.getPublic());
        assertInstanceOf(ECPrivateKey.class, keyPair.getPrivate());
    }

    @Test
    public void test_nullKeyPair_throwsException() {
        // Arrange
        KeyPair keyPair = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> SecurityUtil.getPublicKey(keyPair));
    }

    @Test
    public void test_invalidKeyPair_throwsException() {
        // Arrange
        KeyPair keyPair = mock(KeyPair.class);
        PublicKey publicKey = mock(PublicKey.class);
        when(keyPair.getPublic()).thenReturn(publicKey);

        // Act and Assert
        assertThrows(ClassCastException.class, () -> SecurityUtil.getPublicKey(keyPair));
    }

    //    @Test
    //    public void getECKey() {
    //        EphemPubKey ephemPubKey = new EphemPubKey();
    //        ephemPubKey.setCrv("P-256");
    //        ephemPubKey.setX("qT6cvxue8s2I4a-v50n7WvNWlELH_YTXo_eC4Zho0FM");
    //        ephemPubKey.setY("5bAVo1XQ9u68cv0t_5JYFf8R2OuVYByj5h_dGKln-gs");
    //
    //        ECKey ecKey = SecurityUtil.getECKey(ephemPubKey);
    //        assertNotNull(ecKey);
    //    }
    //
    //    @Test
    //    public void test_successfully_load_keystore_and_get_RSA_key_pair() throws Exception {
    //        // Mock dependencies
    //        SignerDetail signerDetail = mock(SignerDetail.class);
    //        List<Base64> x509CertChain = new ArrayList<>();
    //        x509CertChain.add(new Base64("Dummt"));
    //
    //        // Set up mock behavior
    //        String keyPass = "password";
    //        String keyStore = "keystore.jks";
    //        String signerKeyPair = "keyPair";
    //        RSAPublicKey publicKey = mock(RSAPublicKey.class);
    //        RSAPrivateKey privateKey = mock(RSAPrivateKey.class);
    //
    //        when(signerDetail.getKeypass()).thenReturn(keyPass);
    //        when(signerDetail.getKeystore()).thenReturn(keyStore);
    //        when(signerDetail.getSignerKeyPair()).thenReturn(signerKeyPair);
    //
    //        X509Certificate rsaPublicKey = mock(X509Certificate.class);
    //        RSAPrivateKey rsaPrivateKey = mock(RSAPrivateKey.class);
    //        when(X509CertUtils.parse((byte[]) any())).thenReturn(rsaPublicKey);
    //
    //
    //        // Invoke the method
    //        KeyPair result = SecurityUtil.getRSAKeyPairFromKeystore(signerDetail, x509CertChain);
    //
    //        // Verify the result
    //        assertNotNull(result);
    //        assertEquals(publicKey, result.getPublic());
    //        assertEquals(privateKey, result.getPrivate());
    //    }
    //
    //    @Test
    //    public void test_generateDigitalSignatureWithPS256_ValidKeypairAndX5CCertificateChain()
    // throws SignerServiceException {
    //        // Arrange
    //        KeyPair keyPair = mock(KeyPair.class);
    //        List<Base64> x5c = new ArrayList<>();
    //        x5c.add(Base64.encode("certificate".getBytes()));
    //        String jwsPayload = "payload";
    //
    //        // Act
    //        String result = SecurityUtil.generateDigitalSignatureWithPS256(keyPair, x5c,
    // jwsPayload);
    //
    //        // Assert
    //        assertNotNull(result);
    //    }
    //    @Test
    //    public void test_generateDigitalSignatureWithPS256_NullOrEmptyX5CCertificateChain() {
    //        // Arrange
    //        KeyPair keyPair = mock(KeyPair.class);
    //        String jwsPayload = "payload";
    //        RSAPrivateKey rsaPrivateKey = mock(RSAPrivateKey.class);
    //        when(keyPair.getPrivate()).thenReturn(rsaPrivateKey);
    //        RSASSASigner signer = mock(RSASSASigner.class);
    //        when(new RSASSASigner(rsaPrivateKey)).thenReturn(signer);
    //
    //
    //
    //
    //
    //        // Act & Assert
    //        assertThrows(SignerServiceException.class, () -> {
    //            SecurityUtil.generateDigitalSignatureWithPS256(keyPair, null, jwsPayload);
    //        });
    //
    //        assertThrows(SignerServiceException.class, () -> {
    //            SecurityUtil.generateDigitalSignatureWithPS256(keyPair, new ArrayList<>(),
    // jwsPayload);
    //        });
    //    }

}
