package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.MasterCardConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AuthValueConfig;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGenerator;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "masterCardAuthValueGeneratorImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MasterCardAuthValueGeneratorImpl implements AuthValueGenerator {

    private final AuthValueConfig authValueConfig;

    @Override
    public String createAuthValue(Transaction transaction) throws ACSException {

        try {
            String acsKey = authValueConfig.getMasterCardAcsKey();

            String dsTransId = transaction.getTransactionReferenceDetail().getDsTransactionId();

            dsTransId = dsTransId.replace("-", "");

            String PAN = transaction.getTransactionCardDetail().getCardNumber();

            String extendedPAN =
                    Util.padString(
                            PAN, 20, InternalConstants.SYMBOL_F, InternalConstants.PAD_RIGHT);

            String data = extendedPAN + dsTransId;

            final Mac sha256HMAC = Mac.getInstance(MasterCardConstants.HMACSHA256_ALGORITHM);
            final SecretKeySpec secretkey =
                    new SecretKeySpec(
                            org.apache.commons.codec.binary.Hex.decodeHex(acsKey.toCharArray()),
                            MasterCardConstants.HMACSHA256_ALGORITHM);

            sha256HMAC.init(secretkey);
            final byte[] byteData =
                    sha256HMAC.doFinal(
                            org.apache.commons.codec.binary.Hex.decodeHex(data.toCharArray()));
            final byte[] arrayToConvert = Arrays.copyOfRange(byteData, 0, 4);

            String encodedHexadecimal =
                    org.apache.commons.codec.binary.Hex.encodeHexString(arrayToConvert)
                            .toUpperCase();
            log.debug("4 bytes IAV:" + encodedHexadecimal);

            String iavValue =
                    MasterCardConstants.TLV_TAG
                            + encodedHexadecimal
                            + MasterCardConstants.ZERO_PADDING;
            return Base64.encodeBase64String(
                    org.apache.commons.codec.binary.Hex.decodeHex(iavValue.toCharArray()));

        } catch (NoSuchAlgorithmException | InvalidKeyException | DecoderException e) {
            log.error("Error while deriving the MasterCard AAV value: ", e);
            throw new ACSException(
                    InternalErrorCode.HSM_INTERNAL_EXCEPTION,
                    "Error while deriving the MasterCard AAV value",
                    e);
        }
    }
}
