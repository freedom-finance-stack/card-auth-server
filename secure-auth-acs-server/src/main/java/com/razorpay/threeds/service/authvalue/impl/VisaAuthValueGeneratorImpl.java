package com.razorpay.threeds.service.authvalue.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.constant.ThreeDSConstant;
import com.razorpay.threeds.constant.VISAConstant;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.authvalue.AuthValueGenerator;
import com.razorpay.threeds.service.authvalue.hsm.CAVVGenerationService;
import com.razorpay.threeds.utils.HexUtil;
import com.razorpay.threeds.utils.Util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.acs.dao.contract.utils.Util.DATE_FORMAT_YYDDD;
import static com.razorpay.threeds.constant.InternalConstants.PADDED_SYMBOL_0;
import static com.razorpay.threeds.constant.InternalConstants.PAD_LEFT;
import static com.razorpay.threeds.constant.InternalConstants.PAD_RIGHT;
import static com.razorpay.threeds.constant.VISAConstant.CAVV_KEY_INDICATOR;
import static com.razorpay.threeds.constant.VISAConstant.CAVV_VERSION_7;
import static com.razorpay.threeds.constant.VISAConstant.PROTOCOL_VERSION_EMV_2_1_0;

@Slf4j
@Service(value = "visaAuthValueGeneratorServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisaAuthValueGeneratorImpl implements AuthValueGenerator {

  private final CAVVGenerationService cavvGenerationService;

  @Override
  public String createCAVV(Transaction transaction) throws ACSException {

    String pan = transaction.getTransactionCardDetail().getCardNumber();

    String strAuthenticationResultCode =
        getAuthenticationResultCode(transaction.getTransactionStatus());

    String secondFactorAuthCode = getAuthenticationMethodCode(transaction);

    String seedATN = String.valueOf(Util.generateRandomNumber());
    log.debug("generateCAVV() atn: {}", seedATN);

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
        sixteenDigitNumericValue + seedATN + strAuthenticationResultCode + secondFactorAuthCode;
    String data = Util.extendString(cvv, PADDED_SYMBOL_0, 32, PAD_RIGHT);

    String cvvOutput = cavvGenerationService.generateCavv(transaction, data);

    String authMethod = getAuthenticationMethodCode(transaction);

    LocalDateTime objPurchaseDate =
        transaction.getTransactionPurchaseDetail().getPurchaseTimestamp().toLocalDateTime();

    String purchaseDate = String.valueOf(objPurchaseDate.getDayOfYear());
    if (purchaseDate.length() < 3) {
      purchaseDate = Util.extendString(purchaseDate, PADDED_SYMBOL_0, 3, PAD_LEFT);
    }

    long purchaseAmountLong =
        Long.parseLong(transaction.getTransactionPurchaseDetail().getPurchaseAmount());
    String purchaseAmountHexString = Long.toHexString(purchaseAmountLong).toUpperCase();
    purchaseAmountHexString =
        Util.extendString(
            purchaseAmountHexString,
            PADDED_SYMBOL_0,
            10,
            PAD_LEFT); // HexDump.zeropad(hexString, 10);

    String strSupplementalData =
        purchaseAmountHexString
            + transaction.getTransactionPurchaseDetail().getPurchaseCurrency()
            + purchaseDate;

    String cavvVersion = CAVV_VERSION_7 + PROTOCOL_VERSION_EMV_2_1_0;

    String informationalData = getInformationalData(transaction);

    String cavv =
        PADDED_SYMBOL_0
            + strAuthenticationResultCode // 3D Secure Authentication Result Code
            // (Position - 1)
            + authMethod // Authentication Method (Position - 2)
            + CAVV_KEY_INDICATOR // CAVV Key Indicator (Position - 3)
            + PADDED_SYMBOL_0
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
    return Util.extendString(
        transaction.getTransactionPurchaseDetail().getPurchaseAmount(),
        PADDED_SYMBOL_0,
        12,
        PAD_LEFT);
  }

  /**
   * Function to get Authentication Result Code from {@link TransactionStatus}
   *
   * @param transactionStatus
   * @return String: Authentication Result Code
   */
  private String getAuthenticationResultCode(@NonNull final TransactionStatus transactionStatus) {
    return VISAConstant.VISATransactionStatusInfo.getInstance(transactionStatus)
        .getAuthenticationResultCode();
  }

  /**
   * Function to get Authentication Method Code from {@link Transaction}
   *
   * @param transaction
   * @return String: Authentication Method Code
   */
  private String getAuthenticationMethodCode(@NonNull final Transaction transaction) {

    return transaction.isChallengeMandated()
        ? ThreeDSConstant.AuthenticationMethod.CHALLENGE_FLOW_USING_OTP_VIA_SMS.getCode()
        : ThreeDSConstant.AuthenticationMethod.FRICTIONLESS_RBA.getCode();
  }
}
