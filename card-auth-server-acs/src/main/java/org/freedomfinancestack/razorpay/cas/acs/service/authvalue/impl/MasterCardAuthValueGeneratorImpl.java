package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import org.freedomfinancestack.razorpay.cas.acs.constant.MasterCardConstant;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGenerator;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.CVVGenerationService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cryptix.util.core.Hex;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "masterCardAuthValueGeneratorImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MasterCardAuthValueGeneratorImpl implements AuthValueGenerator {

    private static final int ATN_SEED_VALUE_DIGITS = 4;

    private final CVVGenerationService cvvGenerationService;

    @Override
    public String createAuthValue(Transaction transaction)
            throws ACSException, ValidationException {

        String pan = transaction.getTransactionCardDetail().getCardNumber();

        String seedATN = String.valueOf(Util.generateRandomNumber(ATN_SEED_VALUE_DIGITS));
        log.debug("createAuthValue() atn: {}", seedATN);

        String strAuthenticationResultCode =
                getAuthenticationResultCode(transaction.getTransactionStatus());

        String strServiceCode = getServiceCode(strAuthenticationResultCode);

        String data = pan + seedATN + strServiceCode;

        String cvvOutput = cvvGenerationService.generateCVV(transaction, data);

        // Prepare AAV and store it in aavBytes
        byte[] aavBytes = new byte[20];
        int i = 0;

        if (strAuthenticationResultCode.equals(
                MasterCardConstant.MasterCardTransactionStatusInfo.ATTEMPTS.getTxnStatusCode())) {
            aavBytes[i++] = (byte) MasterCardConstant.AAV_CONTROL_BYTE_A;
        } else {
            aavBytes[i++] = (byte) MasterCardConstant.AAV_CONTROL_BYTE_Y;
        }

        // SHA1 Merchant Name
        int usefulBytes = MasterCardConstant.AAV_MERCHANT_NAME_HASH_BYTES;
        String strMerchantName = transaction.getTransactionMerchant().getMerchantName();

        byte[] mercName = formatMerchantName(strMerchantName);

        // Hashing of Merchant Name using HSM call
        byte[] merNameHash = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(mercName);
            merNameHash = md.digest();
        } catch (Exception e) {
            log.error(
                    "getHashValue() Error Occurred while generating hash value for data: {}",
                    merNameHash,
                    e);
        }

        for (; i < (usefulBytes + 1); i++) {
            aavBytes[i] = merNameHash[i - 1];
        }

        // Byte10 and 11
        String strBytes10to11 = "";
        if (strAuthenticationResultCode.equals(
                MasterCardConstant.MasterCardTransactionStatusInfo.ATTEMPTS.getTxnStatusCode())) {
            strBytes10to11 =
                    MasterCardConstant.AAV_ACS_IDENTIFIER
                            + MasterCardConstant.AAV_AUTH_METHOD_ATTEMPT
                            + MasterCardConstant.AAV_BIN_KEY_ID;
        } else {
            strBytes10to11 =
                    MasterCardConstant.AAV_ACS_IDENTIFIER
                            + MasterCardConstant.AAV_AUTH_METHOD_PASSWORD
                            + MasterCardConstant.AAV_BIN_KEY_ID;
        }

        byte[] bytes10to11 = getBCDBytes(strBytes10to11);

        for (int j = 0; j < 2; i++, j++) {
            aavBytes[i] = bytes10to11[j];
        }

        // Txn Seq No
        String txSeqNo = "0000" + seedATN;
        int num1 = Integer.parseInt(txSeqNo);

        byte[] txSeq = Hex.fromString(Integer.toHexString(num1));
        byte[] btxSeq = new byte[4];
        byte bAppendByte = 0;
        int k = 0;
        for (; k < (4 - txSeq.length); k++) {
            btxSeq[k] = bAppendByte;
        }
        for (int l = 0; l < txSeq.length; l++, k++) {
            btxSeq[k] = txSeq[l];
        }
        for (int j = 0; j < 4; i++, j++) {
            aavBytes[i] = btxSeq[j];
        }

        // MAC
        String strBytes16to20 = "0" + cvvOutput + "000000";
        byte[] bytes16to20 = getBCDBytes(strBytes16to20);
        for (int j = 0; j < 2; i++, j++) {
            aavBytes[i] = bytes16to20[j];
        }

        // Base64 Encoding
        String cavv = new String(Base64.getEncoder().encode(aavBytes), StandardCharsets.UTF_8);

        return cavv;
    }

    /**
     * Function to get Authentication Result Code from {@link TransactionStatus}
     *
     * @param transactionStatus
     * @return String: Authentication Result Code
     */
    private String getAuthenticationResultCode(@NonNull final TransactionStatus transactionStatus)
            throws ValidationException {
        MasterCardConstant.MasterCardTransactionStatusInfo instance =
                MasterCardConstant.MasterCardTransactionStatusInfo.getInstance(transactionStatus);

        if (instance == null) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    "Valid transaction status not found");
        }
        return instance.getAuthenticationResultCode();
    }

    /**
     * Function to get Service Code from {@link TransactionStatus}
     *
     * @param strAuthenticationResultCode
     * @return String: Service Code
     */
    private String getServiceCode(@NonNull final String strAuthenticationResultCode)
            throws ValidationException {
        String authMethod = MasterCardConstant.AAV_AUTH_METHOD_PASSWORD;

        if (Integer.parseInt(authMethod) > 9) {
            authMethod = Integer.toString(Integer.parseInt(authMethod) - 10);
        }
        String srvCode = "";
        String controlByte = "";

        if (strAuthenticationResultCode.equals(
                MasterCardConstant.MasterCardTransactionStatusInfo.SUCCESS
                        .getAuthenticationResultCode())) {
            controlByte = Integer.toString(MasterCardConstant.AAV_CONTROL_BYTE_Y);
            srvCode = controlByte.substring(1);
        } else if (strAuthenticationResultCode.equals(
                MasterCardConstant.MasterCardTransactionStatusInfo.ATTEMPTS
                        .getAuthenticationResultCode())) {
            controlByte = Integer.toString(MasterCardConstant.AAV_CONTROL_BYTE_A);
            srvCode = controlByte.substring(1);
        }
        return authMethod.concat(srvCode);
    }

    /**
     * Function to get BCD Bytes
     *
     * @param data
     * @return byte[]: BCD Bytes
     */
    private byte[] getBCDBytes(@NonNull String data) throws ValidationException {
        byte[] pdata = new byte[11];

        for (int z = 0, c = 0; z < data.length(); z += 2, c++) {
            int num = Integer.parseInt(data.substring(z, z + 2));
            byte b = (byte) (((num / 10) << 4) + (num % 10));

            pdata[c] = b;
        }

        return pdata;
    }

    /**
     * Function to format Merchant Name
     *
     * @param strMername
     * @return byte[]: formatted Merchant Name
     */
    private byte[] formatMerchantName(@NonNull String strMername) throws ValidationException {
        strMername = strMername.trim();
        byte[] b = strMername.getBytes();
        int i;

        String formattedStr = "";
        String hexName = Hex.toString(b);
        String exSingleDigit = "";
        exSingleDigit += Integer.toHexString(0x7F);

        String digit = "";
        for (i = 0x0000; i <= 0x001F; i++) {
            digit = Integer.toHexString(i);
            if (digit.length() < 2) {
                digit = "0".concat(digit);
            }
            exSingleDigit += digit;
        }
        exSingleDigit = exSingleDigit.toUpperCase();

        int index = 0;
        for (i = 0; i < exSingleDigit.length(); i += 2) {
            index = 0;
            while (index != -1) {
                index = hexName.indexOf(exSingleDigit.substring(i, i + 2));
                if (index == -1) {
                    break;
                } else {
                    if ((index % 2) == 0) {
                        hexName = hexName.substring(0, index) + hexName.substring(index + 2);
                    } else {
                        i += 2;
                    }
                }
            }
        }

        formattedStr = hexName;
        String exTwoDigits = "";
        exTwoDigits += Integer.toHexString(0xC2AD); // UTF-8 Bytes C2 AD

        // C280 - C2A0
        for (i = 0xC280; i < 0xC2A0; i++) {
            byte b2 = (byte) i;
            exTwoDigits += Integer.toHexString(b2);
        }

        hexName = formattedStr;

        exTwoDigits = exTwoDigits.toUpperCase();

        index = 0;
        for (i = 0; i < exTwoDigits.length(); i += 4) {
            index = 0;
            while (index != -1) {
                index = hexName.indexOf(exTwoDigits.substring(i, i + 4));
                if (index == -1) {
                    break;
                } else {
                    if ((index % 2) == 0) {
                        hexName = hexName.substring(0, index) + hexName.substring(index + 4);
                    } else {
                        i += 4;
                    }
                }
            }
        }

        formattedStr = hexName;

        String exThreeDigits = "";
        for (i = 0xE28080; i <= 0xE281AF; i++) {
            exThreeDigits += Integer.toHexString(i);
        }

        exThreeDigits = exThreeDigits.toUpperCase();
        index = 0;
        for (i = 0; i < exThreeDigits.length(); i += 6) {
            index = 0;
            while (index != -1) {
                index = hexName.indexOf(exThreeDigits.substring(i, i + 6));
                if (index == -1) {
                    break;
                } else {
                    if ((index % 2) == 0) {
                        hexName = hexName.substring(0, index) + hexName.substring(index + 6);
                    } else {
                        i += 6;
                    }
                }
            }
        }

        formattedStr = hexName;
        return Hex.fromString(formattedStr);
    }
}
