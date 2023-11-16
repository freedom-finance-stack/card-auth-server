package org.freedomfinancestack.razorpay.cas.acs.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.freedomfinancestack.razorpay.cas.contract.EphemPubKey;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetail;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jose.util.X509CertUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityUtil {
    public static KeyPair generateEphermalKeyPair()
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(Curve.P_256.toECParameterSpec());
        KeyPair keyPair = gen.generateKeyPair();
        return keyPair;
    }

    public static JWK getPublicKey(KeyPair keyPair) {
        return new ECKey.Builder(Curve.P_256, (ECPublicKey) keyPair.getPublic())
                .privateKey((ECPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    public static ECKey getECKey(EphemPubKey ephemPubKey) throws Exception {
        ECKey ecKey = null;

        if ("P-256".equalsIgnoreCase(ephemPubKey.getCrv())) {
            Base64URL x = new Base64URL(ephemPubKey.getX());
            Base64URL y = new Base64URL(ephemPubKey.getY());
            ecKey = new ECKey.Builder(Curve.P_256, x, y).build();
        }
        return ecKey;
    }

    public static List<Base64> getKeyInfo(SignerDetail signerDetail, String caFilepath) throws IOException {

        List<Base64> x5c = new ArrayList<>();
        KeyStore ks = null;
        Certificate signingCert = null;
        Certificate rootCert = null;
        Certificate interCert = null;

        String keyStorePath = null;
        String keyPassword = null;

        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            try (InputStream caCertFile = new FileInputStream(caFilepath)) {
                X509Certificate caCert = (X509Certificate) cf.generateCertificate(caCertFile);
                byte[] VALUE = caCert.getEncoded();
                x5c.add(Base64.encode(VALUE));
            }catch (Exception e){
                log.error("exception: ", e);
            }


//            String rootCertKey = signerDetail.getRootCertKey();
//            rootCert = ks.getCertificate(rootCertKey);
//            String interCertKey = signerDetail.getInterCertKey();
//            interCert = ks.getCertificate(interCertKey);

//            // Inter - no intermediate cert in case of UL testing
//            if (interCert instanceof java.security.cert.X509Certificate) {
//                java.security.cert.X509Certificate x509cert =
//                        (java.security.cert.X509Certificate) interCert;
//                byte[] VALUE = x509cert.getEncoded();
//                Base64 encodedValue = Base64.encode(VALUE);
//                x5c.add(encodedValue);
//            }
//
//            // Root
//            if (rootCert instanceof java.security.cert.X509Certificate) {
//                java.security.cert.X509Certificate x509cert =
//                        (java.security.cert.X509Certificate) rootCert;
//                byte[] VALUE = x509cert.getEncoded();
//                Base64 encodedValue = Base64.encode(VALUE);
//                x5c.add(encodedValue);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return x5c;
    }

    public static KeyPair getRSAKeyPairFromKeystore(
            SignerDetail signerDetail, List<Base64> x509CertChain) throws Exception {

        String keyStore = signerDetail.getKeystore();
        String keyPass = signerDetail.getKeypass();

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(keyStore), keyPass.toCharArray());

        RSAPublicKey publicKey =
                (RSAPublicKey) X509CertUtils.parse(x509CertChain.get(0).decode()).getPublicKey();
        RSAPrivateKey privateKey =
                (RSAPrivateKey) ks.getKey(signerDetail.getSignerKeyPair(), keyPass.toCharArray());

        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return keyPair;
    }

    public static String generateDigitalSignatureWithPS256(
            KeyPair keyPair, List<Base64> x5c, String jwsPayload) throws Exception {

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // Need BouncyCastle for PSS
        Security.addProvider(BouncyCastleProviderSingleton.getInstance());

        RSASSASigner signer = new RSASSASigner(privateKey);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.PS256).x509CertChain(x5c).build();

        Payload payload = new Payload(jwsPayload);
        JWSObject jwsObject = new JWSObject(header, payload);

        String s = jwsObject.getHeader().toBase64URL() + "." + jwsObject.getPayload().toBase64URL();

        jwsObject.sign(signer);

        String signedContent = s + "." + jwsObject.getSignature();

        return signedContent;
    }
}