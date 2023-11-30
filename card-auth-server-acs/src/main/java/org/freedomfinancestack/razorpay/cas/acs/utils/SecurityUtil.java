package org.freedomfinancestack.razorpay.cas.acs.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.SignerServiceException;
import org.freedomfinancestack.razorpay.cas.contract.EphemPubKey;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetail;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jose.util.X509CertUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtil {
    public static KeyPair generateEphermalKeyPair() throws SignerServiceException {

        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
            gen.initialize(Curve.P_256.toECParameterSpec());
            return gen.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_ALGORITHM_EXCEPTION,
                    ex);
        }
    }

    public static JWK getPublicKey(KeyPair keyPair) {
        return new ECKey.Builder(Curve.P_256, (ECPublicKey) keyPair.getPublic())
                .privateKey((ECPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    public static ECKey getECKey(EphemPubKey ephemPubKey) {
        ECKey ecKey = null;

        if ("P-256".equalsIgnoreCase(ephemPubKey.getCrv())) {
            Base64URL x = new Base64URL(ephemPubKey.getX());
            Base64URL y = new Base64URL(ephemPubKey.getY());
            ecKey = new ECKey.Builder(Curve.P_256, x, y).build();
        }
        return ecKey;
    }

    public static List<Base64> getKeyInfo(SignerDetail signerDetail) throws SignerServiceException {

        List<Base64> x5c = new ArrayList<>();

        KeyStore ks;
        Certificate signingCert;
        Certificate rootCert;
        Certificate interCert;

        String keyPassword;
        String keystore;

        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // TODO: Assuming Keypass as decrypted for not, need to store the encrypted one here
            keyPassword = signerDetail.getKeypass();
            keystore = signerDetail.getKeystore();
            ks.load(new FileInputStream(keystore), keyPassword.toCharArray());

            // Get Signing, Root and Inter Certificate from KeyStore
            String signerCertKey = signerDetail.getSignerCertKey();
            signingCert = ks.getCertificate(signerCertKey);
            String rootCertKey = signerDetail.getRootCertKey();
            rootCert = ks.getCertificate(rootCertKey);
            String interCertKey = signerDetail.getInterCertKey();
            interCert = ks.getCertificate(interCertKey);

            // Signing
            if (signingCert instanceof java.security.cert.X509Certificate) {
                java.security.cert.X509Certificate x509cert =
                        (java.security.cert.X509Certificate) signingCert;
                byte[] VALUE = x509cert.getEncoded();
                Base64 encodedValue = Base64.encode(VALUE);
                x5c.add(encodedValue);
            }

            // Inter - no intermediate cert in case of UL testing
            if (interCert instanceof java.security.cert.X509Certificate) {
                java.security.cert.X509Certificate x509cert =
                        (java.security.cert.X509Certificate) interCert;
                byte[] VALUE = x509cert.getEncoded();
                Base64 encodedValue = Base64.encode(VALUE);
                x5c.add(encodedValue);
            }

            // Root
            if (rootCert instanceof java.security.cert.X509Certificate) {
                java.security.cert.X509Certificate x509cert =
                        (java.security.cert.X509Certificate) rootCert;
                byte[] VALUE = x509cert.getEncoded();
                Base64 encodedValue = Base64.encode(VALUE);
                x5c.add(encodedValue);
            }

            return x5c;
        } catch (CertificateException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_CERTIFICATE_EXCEPTION,
                    ex);
        } catch (KeyStoreException | IOException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_KEY_STORE_EXCEPTION,
                    ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_ALGORITHM_EXCEPTION,
                    ex);
        }
    }

    public static KeyPair getRSAKeyPairFromKeystore(
            SignerDetail signerDetail, List<Base64> x509CertChain) throws SignerServiceException {

        try {
            String keyPass = signerDetail.getKeypass();
            String keyStore = signerDetail.getKeystore();
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream(keyStore), keyPass.toCharArray());

            RSAPublicKey publicKey =
                    (RSAPublicKey)
                            X509CertUtils.parse(x509CertChain.get(0).decode()).getPublicKey();
            RSAPrivateKey privateKey =
                    (RSAPrivateKey)
                            ks.getKey(signerDetail.getSignerKeyPair(), keyPass.toCharArray());

            return new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException | IOException | UnrecoverableKeyException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_KEY_STORE_EXCEPTION,
                    ex);
        } catch (CertificateException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_CERTIFICATE_EXCEPTION,
                    ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_ALGORITHM_EXCEPTION,
                    ex);
        }
    }

    public static String generateDigitalSignatureWithPS256(
            KeyPair keyPair, List<Base64> x5c, String jwsPayload) throws SignerServiceException {

        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // Need BouncyCastle for PSS
            Security.addProvider(BouncyCastleProviderSingleton.getInstance());

            RSASSASigner signer = new RSASSASigner(privateKey);

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.PS256).x509CertChain(x5c).build();

            Payload payload = new Payload(jwsPayload);
            JWSObject jwsObject = new JWSObject(header, payload);

            String s =
                    jwsObject.getHeader().toBase64URL()
                            + "."
                            + jwsObject.getPayload().toBase64URL();

            jwsObject.sign(signer);

            return s + "." + jwsObject.getSignature();
        } catch (JOSEException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_JOSE_EXCEPTION,
                    ex);
        }
    }
}