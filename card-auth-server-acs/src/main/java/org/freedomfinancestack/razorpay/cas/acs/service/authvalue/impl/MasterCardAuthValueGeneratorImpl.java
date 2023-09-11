package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
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

    @Override
    public String createAuthValue(Transaction transaction)
            throws ACSException, ValidationException {
        String iav = null;

        try {
            String acsKey = "B039878C1F96D212F509B2DC4CC8CD1BB039878C1F96D212F509B2DC4CC8CD1B";

            String dsTransId = transaction.getTransactionReferenceDetail().getDsTransactionId();

            dsTransId = dsTransId.replace("-", "");

            String PAN = transaction.getTransactionCardDetail().getCardNumber();

            String extendedPAN = Util.extendString(PAN, InternalConstants.SYMBOL_F, 20);

            String data = extendedPAN + dsTransId;

            final Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            final SecretKeySpec secretkey =
                    new SecretKeySpec(
                            org.apache.commons.codec.binary.Hex.decodeHex(acsKey.toCharArray()),
                            "HmacSHA256");

            sha256HMAC.init(secretkey);
            final byte[] byteData =
                    sha256HMAC.doFinal(
                            org.apache.commons.codec.binary.Hex.decodeHex(data.toCharArray()));
            final byte[] arrayToConvert = Arrays.copyOfRange(byteData, 0, 4);

            String encodedHexadecimal =
                    org.apache.commons.codec.binary.Hex.encodeHexString(arrayToConvert)
                            .toUpperCase();
            log.debug("4 bytes IAV:" + encodedHexadecimal);

            String iavValue = "C604" + encodedHexadecimal + "000000000000000000000000000000";
            iav =
                    Base64.encodeBase64String(
                            org.apache.commons.codec.binary.Hex.decodeHex(iavValue.toCharArray()));
            log.debug("base64IAV value :" + iav);

        } catch (Exception e) {
            String message =
                    "generateIAVWithSPA2: "
                            + e.getStackTrace()
                            + " "
                            + "Error while deriving the MasterCard AAV value";
            log.debug(message);
            throw new ACSException(
                    InternalErrorCode.HSM_INTERNAL_EXCEPTION,
                    "Error while deriving the MasterCard AAV value",
                    e);
        }

        return iav;
    }
}
