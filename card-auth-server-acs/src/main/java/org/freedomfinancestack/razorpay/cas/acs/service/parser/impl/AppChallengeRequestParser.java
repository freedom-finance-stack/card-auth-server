package org.freedomfinancestack.razorpay.cas.acs.service.parser.impl;

import java.util.Arrays;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.ChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.utils.HexUtil;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.ChallengeCancelIndicator;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppChallengeRequestParser implements ChallengeRequestParser {
    private final TransactionService transactionService;
    private final TestConfigProperties testConfigProperties;

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
            String decryptedCReq;
            if (testConfigProperties.isEnableDecryptionEncryption()) {
                if (!Util.validateBase64UrlEncodedString(strCReq)) {
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

    private String decryptCReq(String encryptedCReq)
            throws ParseException, TransactionDataNotValidException {
        String decryptedCReq;
        byte[] acsKDFSecretKey;
        Transaction transaction;

        // Step 6 - ACS to Parse JWE object received from SDK
        JWEObject acsJweObject;
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
                    .setEncryptionMethod(acsJweObject.getHeader().getEncryptionMethod().getName());

            transactionService.saveOrUpdate(transaction);

            if (acsJweObject.getHeader().getEncryptionMethod().getName().equals("A128GCM")) {
                acsKDFSecretKey =
                        Arrays.copyOfRange(acsKDFSecretKey, 0, acsKDFSecretKey.length / 2);
            }
            // Step 7 - ASC to decrypt the JWE object using ACS CEK
            acsJweObject.decrypt(new DirectDecrypter(acsKDFSecretKey));

            // Step 8 - ACS to fetch the CREQ for processing
            decryptedCReq = acsJweObject.getPayload().toString();
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
}
