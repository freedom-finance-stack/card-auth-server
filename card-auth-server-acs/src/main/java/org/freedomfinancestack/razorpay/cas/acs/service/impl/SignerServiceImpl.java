package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;

import org.freedomfinancestack.razorpay.cas.acs.dto.SignedContent;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.SignerServiceException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.SignerService;
import org.freedomfinancestack.razorpay.cas.acs.utils.HexUtil;
import org.freedomfinancestack.razorpay.cas.acs.utils.SecurityUtil;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.EphemPubKey;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetail;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetailPK;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.SignerDetailRepository;
import org.nimbusds.jose.crypto.CustomConcatKDF;
import org.nimbusds.jose.crypto.CustomECDH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignerServiceImpl implements SignerService {
    private final SignerDetailRepository signerDetailRepository;

    private final TestConfigProperties testConfigProperties;

    @Override
    public String getAcsSignedContent(AREQ areq, Transaction transaction, String acsUrl)
            throws SignerServiceException {
        SignerDetail signerDetail;
        String signedData;

        // 116
        // step 1 : Generates a fresh ephemeral key pair (QT, dT) as described in Annex C and
        // makes QT available for inclusion in the ARes message.
        // Step 1 - Create Ephermal Keyair
        KeyPair acsKeyPair = SecurityUtil.generateEphermalKeyPair();

        // step 2 : Checks that QC is a point on the curve P-256.
        // Step 2 - Create JWK with Public key info (Nimbus Jose)
        JWK acsPublicKeyJWK = SecurityUtil.getPublicKey(acsKeyPair);

        // step 3 : Completes the Diffie-Hellman key exchange process as a local mechanism
        // according to JWA (RFC 7518) in Direct Key Agreement mode using curve P-256, dT and QC
        // to produce a pair of CEKs (one for each direction) which are identified by the ACS
        // Transaction ID.
        // Step 3 -  Generate ECPublicKey from JWK (Nimbus Jose)
        ECKey sdkPubKey = SecurityUtil.getECKey(areq.getSdkEphemPubKey());

        // Generate shared key
        generateSHA256SecretKey(areq, transaction, sdkPubKey, acsKeyPair);

        EphemPubKey acsPublicKey = new EphemPubKey();

        for (Map.Entry<String, ?> entry : acsPublicKeyJWK.getRequiredParams().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            switch (key) {
                case "crv" -> acsPublicKey.setCrv(value);
                case "kty" -> acsPublicKey.setKty(value);
                case "x" -> acsPublicKey.setX(value);
                case "y" -> acsPublicKey.setY(value);
                default -> {}
            }
        }

        SignedContent signedContent = new SignedContent();
        signedContent.setAcsEphemPubKey(acsPublicKey);
        signedContent.setSdkEphemPubKey(areq.getSdkEphemPubKey());
        signedContent.setAcsURL(acsUrl);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String signedJsonObject = gson.toJson(signedContent);

        Byte networkCode = transaction.getTransactionCardDetail().getNetworkCode();
        Optional<SignerDetail> signerDetailOptional =
                signerDetailRepository.findById(
                        new SignerDetailPK(transaction.getInstitutionId(), networkCode));

        if (signerDetailOptional.isPresent()) signerDetail = signerDetailOptional.get();
        else {
            log.debug(
                    "Can't find signerDetail for institution_id: "
                            + transaction.getInstitutionId());
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_DETAIL_NOT_FOUND,
                    "Signer Detail not found");
        }

        List<Base64> x509CertChain = SecurityUtil.getKeyInfo(signerDetail);

        KeyPair keyPair = SecurityUtil.getRSAKeyPairFromKeystore(signerDetail, x509CertChain);
        signedData =
                SecurityUtil.generateDigitalSignatureWithPS256(
                        keyPair, x509CertChain, signedJsonObject);

        return signedData;
    }

    private void generateSHA256SecretKey(
            AREQ areq, Transaction transaction, ECKey sdkPubKey, KeyPair acsKeyPair)
            throws SignerServiceException {

        try {
            // Step 4 - Perform KeyAgreement and derive SecretKey
            SecretKey Z =
                    CustomECDH.deriveSharedSecret(
                            sdkPubKey.toECPublicKey(),
                            (ECPrivateKey) acsKeyPair.getPrivate(),
                            null);

            CustomConcatKDF concatKDF = new CustomConcatKDF("SHA-256");

            String algIdString = "";
            String partyVInfoString = areq.getSdkReferenceNumber();
            int keylength = 256; // A128CBC-HS256

            byte[] algID =
                    CustomConcatKDF.encodeDataWithLength(
                            algIdString.getBytes(StandardCharsets.UTF_8));
            byte[] partyUInfo = CustomConcatKDF.encodeDataWithLength(new byte[0]);
            byte[] partyVInfo =
                    CustomConcatKDF.encodeDataWithLength(
                            partyVInfoString.getBytes(StandardCharsets.UTF_8));
            byte[] suppPubInfo = CustomConcatKDF.encodeIntData(keylength);
            byte[] suppPrivInfo = CustomConcatKDF.encodeNoData();

            SecretKey derivedKey =
                    concatKDF.deriveKey(
                            Z, keylength, algID, partyUInfo, partyVInfo, suppPubInfo, suppPrivInfo);

            transaction
                    .getTransactionSdkDetail()
                    .setAcsSecretKey(HexUtil.byteArrayToHexString(derivedKey.getEncoded()));
        } catch (JOSEException ex) {
            throw new SignerServiceException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.SIGNER_SERVICE_JOSE_EXCEPTION,
                    ex);
        }
    }

    @Override
    public String generateEncryptedResponse(Transaction transaction, CRES cres)
            throws ACSException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String strCRes = gson.toJson(cres);
        String encryptedCRes;
        if (testConfigProperties.isEnableDecryptionEncryption()) {
            encryptedCRes = encryptResponse(transaction, strCRes);
        } else {
            encryptedCRes = strCRes;
        }
        return encryptedCRes;
    }

    private String encryptResponse(Transaction transaction, String challangeResponse)
            throws ACSException {

        String encryptedCRes;
        byte[] acsKDFSecretKey;

        // Step 6 - ACS to Parse JWE object received from SDK
        JWEObject acsJweObject;
        EncryptionMethod encryptionMethod = EncryptionMethod.A128CBC_HS256;

        try {
            String acsTransactionID = transaction.getId();
            String strAcsSecretKey = transaction.getTransactionSdkDetail().getAcsSecretKey();

            acsKDFSecretKey = HexUtil.hexStringToByteArray(strAcsSecretKey);

            if (transaction.getTransactionSdkDetail().getEncryptionMethod().equals("A128GCM")) {
                acsKDFSecretKey =
                        Arrays.copyOfRange(
                                acsKDFSecretKey,
                                acsKDFSecretKey.length / 2,
                                acsKDFSecretKey.length);
                encryptionMethod = EncryptionMethod.A128GCM;
            }

            JWEHeader acsEncHeader =
                    new JWEHeader.Builder(JWEAlgorithm.DIR, encryptionMethod)
                            .keyID(acsTransactionID)
                            .build();

            // Step 3 - Create JWE object
            acsJweObject =
                    new JWEObject(
                            acsEncHeader,
                            new Payload(challangeResponse)); // JWE Object with Header and Payload
            // (CRES/Error)

            // Step 4 - Encrypt the JWE using SDK CEK
            acsJweObject.encrypt(new DirectEncrypter(acsKDFSecretKey)); // JWE Compact SErialization

            // Step 5 - Serialise to compact JOSE form and send it to ACS
            encryptedCRes = acsJweObject.serialize();

        } catch (Exception e) {
            throw new ACSException(InternalErrorCode.CRES_ENCRYPTION_ERROR, e);
        }

        return encryptedCRes;
    }
}
