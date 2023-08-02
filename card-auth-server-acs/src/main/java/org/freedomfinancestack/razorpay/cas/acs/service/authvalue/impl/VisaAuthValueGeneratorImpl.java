package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.freedomfinancestack.razorpay.cas.acs.constant.AuthenticationMethod;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.VISAConstant;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGenerator;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.CVVGenerationService;
import org.freedomfinancestack.razorpay.cas.acs.utils.HexUtil;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.contract.utils.Util.DATE_FORMAT_YYDDD;

@Slf4j
@Service(value = "visaAuthValueGeneratorServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisaAuthValueGeneratorImpl implements AuthValueGenerator {

    private static final int ATN_SEED_VALUE_DIGITS = 4;

    private final CVVGenerationService cvvGenerationService;

    @Override
    public String createAuthValue(Transaction transaction)
            throws ACSException, ValidationException {

        String pan = transaction.getTransactionCardDetail().getCardNumber();

        String strAuthenticationResultCode =
                getAuthenticationResultCode(transaction.getTransactionStatus());

        String secondFactorAuthCode = getAuthenticationMethodCode(transaction);

        String seedATN = String.valueOf(Util.generateRandomNumber(ATN_SEED_VALUE_DIGITS));
        log.debug("createAuthValue() atn: {}", seedATN);

        String eci = transaction.getEci();

        // Extract the PAN sent in the AReq message and replace the last digit of the PAN with
        // the non-zero value (rightmost digit) of the ECI applicable for the current authentication
        // request.
        String nonConvertedPan = pan.substring(0, pan.length() - 1);
        String nonEci = eci.substring(eci.length() - 1);
        String convertedPan = nonConvertedPan + nonEci;

        String supplementalData = getSupplementalData(transaction);

        String str = convertedPan + supplementalData;

        String hashedValue = Util.getHashValue(str);

        String sixteenDigitNumericValue = Util.get16DigitNumericValue(hashedValue);

        // Place the resultant value from step 5, the Seed value, Authentication results code,
        // Second Factor Authentication Code into a 128-bit field padded to the right with binary
        // zeros.
        String cvv =
                sixteenDigitNumericValue
                        + seedATN
                        + strAuthenticationResultCode
                        + secondFactorAuthCode;
        String data =
                Util.padString(
                        cvv, 32, InternalConstants.PADDED_SYMBOL_0, InternalConstants.PAD_RIGHT);

        String cvvOutput = cvvGenerationService.generateCVV(transaction, data);

        String authMethod = getAuthenticationMethodCode(transaction);

        LocalDateTime objPurchaseDate =
                transaction.getTransactionPurchaseDetail().getPurchaseTimestamp().toLocalDateTime();

        String purchaseDate = String.valueOf(objPurchaseDate.getDayOfYear());
        if (purchaseDate.length() < 3) {
            purchaseDate =
                    Util.padString(
                            purchaseDate,
                            3,
                            InternalConstants.PADDED_SYMBOL_0,
                            InternalConstants.PAD_LEFT);
        }

        long purchaseAmountLong =
                Long.parseLong(transaction.getTransactionPurchaseDetail().getPurchaseAmount());
        String purchaseAmountHexString = Long.toHexString(purchaseAmountLong).toUpperCase();
        purchaseAmountHexString =
                Util.padString(
                        purchaseAmountHexString,
                        10,
                        InternalConstants.PADDED_SYMBOL_0,
                        InternalConstants.PAD_LEFT); // HexDump.zeropad(hexString, 10);

        String strSupplementalData =
                purchaseAmountHexString
                        + transaction.getTransactionPurchaseDetail().getPurchaseCurrency()
                        + purchaseDate;

        String cavvVersion = VISAConstant.CAVV_VERSION_7 + VISAConstant.PROTOCOL_VERSION_EMV_2_1_0;

        String informationalData = getInformationalData(transaction);

        String cavv =
                InternalConstants.PADDED_SYMBOL_0
                        + strAuthenticationResultCode // 3D Secure Authentication Result Code
                        // (Position - 1)
                        + authMethod // Authentication Method (Position - 2)
                        + VISAConstant.CAVV_KEY_INDICATOR // CAVV Key Indicator (Position - 3)
                        + InternalConstants.PADDED_SYMBOL_0
                        + cvvOutput // CAVV Value (Position - 4)
                        + seedATN // Seed Value (Position - 5)
                        + strSupplementalData // Supplemental data (Position - 6)
                        + cavvVersion // CAVV Version  (Position - 7)
                        + informationalData; // Informational Data (Position - 8)
        byte[] pdata = HexUtil.hexStringToByteArray(cavv);
        cavv = new String(Base64.getEncoder().encode(pdata), StandardCharsets.UTF_8);
        return cavv;
    }

    private static String getInformationalData(@NonNull final Transaction transaction) {
        String informationalData = "00000000";

        if (transaction.getTransactionMerchant() != null
                && StringUtils.isNoneBlank(transaction.getTransactionMerchant().getMerchantName())
                && transaction.getTransactionBrowserDetail() != null
                && StringUtils.isNotBlank(transaction.getTransactionBrowserDetail().getIp())) {
            informationalData =
                    transaction.getTransactionMerchant().getMerchantName().toUpperCase()
                            + transaction.getTransactionBrowserDetail().getIp();
        }
        // todo check informational Data logic as merchant name is causing issue as
        // Hexadecimal strings can only contain characters from the range 0-9 and A-F (or a-f) to
        // represent the values 0-15.
        informationalData = "00000000";
        return informationalData;
    }

    private String getSupplementalData(@NonNull final Transaction transaction) {
        // Extract the Purchase Amount, Purchase Currency Code from the authentication request
        // and identify the Authentication Date
        String authenticationAmount = getAuthenticationAmount(transaction);
        String authenticationCurrency =
                transaction.getTransactionPurchaseDetail().getPurchaseCurrency();

        String authenticationDate = getAuthenticationDate();

        return authenticationAmount + authenticationCurrency + authenticationDate;
    }

    private String getAuthenticationDate() {
        // DateTimeFormatter.ofPattern("yyDDD");
        return OffsetDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT_YYDDD));
    }

    private String getAuthenticationAmount(Transaction transaction) {
        return Util.padString(
                transaction.getTransactionPurchaseDetail().getPurchaseAmount(),
                12,
                InternalConstants.PADDED_SYMBOL_0,
                InternalConstants.PAD_LEFT);
    }

    /**
     * Function to get Authentication Result Code from {@link TransactionStatus}
     *
     * @param transactionStatus
     * @return String: Authentication Result Code
     */
    private String getAuthenticationResultCode(@NonNull final TransactionStatus transactionStatus)
            throws ValidationException {
        VISAConstant.VISATransactionStatusInfo instance =
                VISAConstant.VISATransactionStatusInfo.getInstance(transactionStatus);

        if (instance == null) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    "Valid transaction status not found");
        }
        return instance.getAuthenticationResultCode();
    }

    /**
     * Function to get Authentication Method Code from {@link Transaction}
     *
     * @param transaction
     * @return String: Authentication Method Code
     */
    private String getAuthenticationMethodCode(@NonNull final Transaction transaction) {

        return transaction.isChallengeMandated()
                ? AuthenticationMethod.CHALLENGE_FLOW_USING_OTP_VIA_SMS.getCode()
                : AuthenticationMethod.FRICTIONLESS_RBA.getCode();
    }
}
