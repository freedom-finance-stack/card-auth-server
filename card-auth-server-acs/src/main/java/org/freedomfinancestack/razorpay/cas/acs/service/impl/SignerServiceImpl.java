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
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.EncryptionDecryptionException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.SignerService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.utils.HexUtil;
import org.freedomfinancestack.razorpay.cas.acs.utils.SecurityUtil;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.EphemPubKey;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.ChallengeCancelIndicator;
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
import com.nimbusds.jose.crypto.DirectDecrypter;
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

    private final TransactionService transactionService;

    private final TestConfigProperties testConfigProperties;

    @Override
    public String getAcsSignedContent(AREQ areq, Transaction transaction, String acsUrl)
            throws EncryptionDecryptionException {
        SignerDetail signerDetail = null;
        String signedData = null;

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
            throw new EncryptionDecryptionException(
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

    @Override
    public CREQ parseEncryptedRequest(String strCReq)
            throws ParseException, TransactionDataNotValidException {
        if (Util.isNullorBlank(strCReq)) {
            throw new ParseException(
                    ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR);
        }
        ThreeDSErrorResponse errorObj = null;
        try {
            errorObj = Util.fromJson(strCReq, ThreeDSErrorResponse.class);
        } catch (Exception e) {
            // Do Nothing
        }
        CREQ objCReq = new CREQ();
        if (errorObj == null) {
            String decryptedCReq = null;
            if (testConfigProperties.isEnableDecryptionEncryption()) {
                if (!Util.isValidBase64Url(strCReq)) {
                    throw new ParseException(
                            ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                            InternalErrorCode.CRES_ENCRYPTION_ERROR);
                }
                decryptedCReq = decryptCReq(strCReq);
            } else {
                decryptedCReq = strCReq;
            }
            try {
                objCReq = Util.fromJson(decryptedCReq, CREQ.class);
            } catch (Exception ex) {
                throw new ParseException(
                        ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                        InternalErrorCode.CREQ_JSON_PARSING_ERROR,
                        ex);
            }
            if (null == objCReq) {
                throw new ParseException(
                        ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                        InternalErrorCode.CREQ_JSON_PARSING_ERROR);
            }
        } else {
            objCReq.setAcsTransID(errorObj.getAcsTransID());
            objCReq.setThreeDSServerTransID(errorObj.getThreeDSServerTransID());
            objCReq.setMessageVersion(errorObj.getMessageVersion());
            objCReq.setMessageType(errorObj.getMessageType());
            objCReq.setChallengeCancel(ChallengeCancelIndicator.TRANSACTION_ERROR.getIndicator());
            objCReq.setSdkTransID(errorObj.getSdkTransID());
        }
        return objCReq;
    }

    @Override
    public String generateEncryptedResponse(Transaction transaction, CRES cres)
            throws ACSException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String strCRes = gson.toJson(cres);
        String encryptedCRes = null;
        if (testConfigProperties.isEnableDecryptionEncryption()) {
            encryptedCRes = encryptResponse(transaction, strCRes);
        } else {
            encryptedCRes = strCRes;
        }
        return encryptedCRes;
    }

    private void generateSHA256SecretKey(
            AREQ areq, Transaction transaction, ECKey sdkPubKey, KeyPair acsKeyPair)
            throws EncryptionDecryptionException {

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
        } catch (Exception ex) {
            throw new EncryptionDecryptionException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    ex);
        }
    }

    private String decryptCReq(String encryptedCReq)
            throws ParseException, TransactionDataNotValidException {
        String decryptedCReq = null;
        byte[] acsKDFSecretKey = null;
        Transaction transaction = null;

        // Step 6 - ACS to Parse JWE object received from SDK
        JWEObject acsJweObject = null;
        try {
            encryptedCReq = encryptedCReq.replaceAll("\\s+", "");
            acsJweObject = JWEObject.parse(encryptedCReq);

            String acsTransactionID = acsJweObject.getHeader().getKeyID();
            transaction = transactionService.findById(acsTransactionID);
            // TODO fetch Only Transaction sdk detail
            if (null == transaction || !transaction.isChallengeMandated()) {
                throw new TransactionDataNotValidException(
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID,
                        InternalErrorCode.TRANSACTION_NOT_FOUND);
            }
            String strAcsSecretKey = transaction.getTransactionSdkDetail().getAcsSecretKey();

            acsKDFSecretKey = HexUtil.hexStringToByteArray(strAcsSecretKey);
            transaction
                    .getTransactionSdkDetail()
                    .setEncryptionAlgorithm(
                            acsJweObject.getHeader().getEncryptionMethod().getName());

            transactionService.saveOrUpdate(transaction);

            if (acsJweObject.getHeader().getEncryptionMethod().getName().equals("A128GCM")) {
                acsKDFSecretKey =
                        Arrays.copyOfRange(acsKDFSecretKey, 0, acsKDFSecretKey.length / 2);
            }
            // Step 7 - ASC to decrypt the JWE object using ACS CEK
            acsJweObject.decrypt(new DirectDecrypter(acsKDFSecretKey));

            // Step 8 - ACS to fetch the CREQ for processing
            String payload = acsJweObject.getPayload().toString();
            decryptedCReq = payload;
        } catch (java.text.ParseException | JOSEException e) {
            throw new ParseException(
                    ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR,
                    e);
        } catch (ACSDataAccessException e) {
            throw new TransactionDataNotValidException(
                    ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID,
                    InternalErrorCode.TRANSACTION_ID_NOT_RECOGNISED);
        }

        return decryptedCReq;
    }

    private String encryptResponse(Transaction transaction, String challangeResponse)
            throws ACSException {

        String encryptedCRes = null;
        byte[] acsKDFSecretKey = null;

        // Step 6 - ACS to Parse JWE object received from SDK
        JWEObject acsJweObject = null;
        EncryptionMethod encryptionMethod = EncryptionMethod.A128CBC_HS256;

        try {
            String acsTransactionID = transaction.getId();
            String strAcsSecretKey = transaction.getTransactionSdkDetail().getAcsSecretKey();

            acsKDFSecretKey = HexUtil.hexStringToByteArray(strAcsSecretKey);

            if (transaction.getTransactionSdkDetail().getEncryptionAlgorithm().equals("A128GCM")) {

                byte[] mdbytes = acsKDFSecretKey;
                byte[] key = new byte[mdbytes.length / 2];

                // for(int I = 0; I < key.length; I++){
                for (int i = 0; i < key.length; i++) {
                    // Choice 1 for using only 128 bits of the 256 generated
                    key[i] = mdbytes[i + (mdbytes.length / 2)];

                    // Choice 2 for using ALL of the 256 bits generated
                    // key[I] = mdbytes[I] ^ mdbytes[I + key.length];
                }
                acsKDFSecretKey = key;
                encryptionMethod = EncryptionMethod.A128GCM;
            }

            JWEHeader acsEncHeader =
                    new JWEHeader.Builder(JWEAlgorithm.DIR, encryptionMethod)
                            .keyID(acsTransactionID)
                            .build();

            // Step 3 - Create JWE object
            // ieyJraWQiOiJhZGM0NjI4Ny0zZWZmLTRjNTUtODVlZC04NGM3MjdjZDU5M2MiLCJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0..jPj3nEb-A-6adCHsUZA02g.8rPub9hscfiH-zuNfYOZuH1nUi3_ZVlxyS1ZEIeUX_26EBytNnCr0bCUhWu1KCC1Ik_euORXAcBZqOpP0cfMt_1FiY4yhH-IE8R1eiyOWx4gggZsgU596-J0k9RyPZvu9mtQ0HPHM6qlV9Iqr_zzLzmh0bPsft5jqyuvsPGLDDNJbp6S95bQVNNTuDFg9zthIDtpsFdTyk2HcMH7Jb3B0UhZRiM4wlUI4onaXBC1S_KRyo_G4w2sYShpU7qVPwIC_VZgPRx1zU_0rMvC0FPAWOBF5oBmNV8aTLJQVfs9wIvNm9tizbyXsitmpHUPnObi.RyJgCU8zpcCgzFVeod9vtAn SDK
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
