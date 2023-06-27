package com.razorpay.threeds.constant;

import com.razorpay.acs.dao.enums.TransactionStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VISAConstant {

  public static final String CAVV_KEY_INDICATOR = "01";

  public static final String CAVV_VERSION_7 = "7";
  public static final String PROTOCOL_VERSION_3DS_1_0_2 = "3";
  public static final String PROTOCOL_VERSION_EMV_2_1_0 = "4";
  public static final String PROTOCOL_VERSION_EMV_2_2_0 = "5";

  public enum VISATransactionStatusInfo {
    SUCCESS(TransactionStatus.SUCCESS, "Y", "0"),
    UNABLE_TO_AUTHENTICATE(TransactionStatus.UNABLE_TO_AUTHENTICATE, "U", "5"),
    FAILED(TransactionStatus.FAILED, "N", "9"),
    REJECTED(TransactionStatus.REJECTED, "R", "9"),
    TRANSACTION_RISK_ANALYSIS_PERFORMED(TransactionStatus.INFORMATIONAL, "I", "2"),
    DATA_SHARE_ONLY(TransactionStatus.INFORMATIONAL, "I", "3"),
    STRONG_CUSTOMER_AUTH_ALREADY_PERFORMED(TransactionStatus.INFORMATIONAL, "I", "4"),
    ATTEMPTS(TransactionStatus.ATTEMPT, "A", "7"),
    ATTEMPTS_WITH_SERVER_NOT_AVAILABLE(TransactionStatus.ATTEMPT, "A", "8");

    VISATransactionStatusInfo(
        TransactionStatus transactionStatus,
        String txnStatusCode,
        String authenticationResultCode) {
      this.transactionStatus = transactionStatus;
      this.txnStatusCode = txnStatusCode;
      this.authenticationResultCode = authenticationResultCode;
    }

    private final TransactionStatus transactionStatus;
    private final String txnStatusCode;
    private final String authenticationResultCode;

    public static VISATransactionStatusInfo getInstance(TransactionStatus transactionStatus) {
      VISATransactionStatusInfo result = null;
      for (VISATransactionStatusInfo visaTransactionStatusInfo :
          VISATransactionStatusInfo.values()) {
        if (transactionStatus.equals(visaTransactionStatusInfo.transactionStatus)) {
          result = visaTransactionStatusInfo;
          break;
        }
      }
      return result;
    }

    public String getTxnStatusCode() {
      return txnStatusCode;
    }

    public String getAuthenticationResultCode() {
      return authenticationResultCode;
    }
  }
}
