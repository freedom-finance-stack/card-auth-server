package org.freedomfinancestack.razorpay.cas.acs.constant;

import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MasterCardConstant {

    public static final String CAVV_KEY_INDICATOR = "01";

    public static final String CAVV_VERSION_7 = "7";
    public static final String PROTOCOL_VERSION_3DS_1_0_2 = "3";
    public static final String PROTOCOL_VERSION_EMV_2_1_0 = "4";
    public static final String PROTOCOL_VERSION_EMV_2_2_0 = "5";

    public static final String AVV_ALGORITHM_SPA = "3";

    public static final String AAV_AUTH_METHOD_ATTEMPT = "0";
    public static final String AAV_AUTH_METHOD_PASSWORD = "1";
    public static final int AAV_CONTROL_BYTE_Y = 0x8C;
    public static final int AAV_CONTROL_BYTE_A = 0x86;
    public static final int AAV_MERCHANT_NAME_HASH_BYTES = 0x8;
    public static final String AAV_ACS_IDENTIFIER = "08";
    public static final String AAV_BIN_KEY_ID = "1";

    @Getter
    @AllArgsConstructor
    public enum MasterCardTransactionStatusInfo {
        SUCCESS(TransactionStatus.SUCCESS, "Y", "0"),
        FAILED(TransactionStatus.FAILED, "N", "9"),
        ATTEMPTS(TransactionStatus.ATTEMPT, "A", "7"),
        UNABLE_TO_AUTHENTICATE(TransactionStatus.UNABLE_TO_AUTHENTICATE, "U", "5");

        private final TransactionStatus transactionStatus;
        private final String txnStatusCode;
        private final String authenticationResultCode;

        public static MasterCardTransactionStatusInfo getInstance(
                TransactionStatus transactionStatus) {
            MasterCardTransactionStatusInfo result = null;
            for (MasterCardTransactionStatusInfo masterCardTransactionStatusInfo :
                    MasterCardTransactionStatusInfo.values()) {
                if (transactionStatus.equals(masterCardTransactionStatusInfo.transactionStatus)) {
                    result = masterCardTransactionStatusInfo;
                    break;
                }
            }
            return result;
        }
    }
}
