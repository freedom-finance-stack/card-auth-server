package org.ffs.razorpay.cas.dao.enums;

import org.ffs.razorpay.cas.contract.enums.MessageCategory;

import lombok.Getter;

@Getter
public enum ECI {
    VISA_SUCCESS("05", TransactionStatus.SUCCESS, Network.VISA),
    VISA_ATTEMPTS("06", TransactionStatus.ATTEMPT, Network.VISA),
    VISA_FAILED("07", TransactionStatus.FAILED, Network.VISA),
    VISA_UNAUTHORIZED("07", TransactionStatus.UNABLE_TO_AUTHENTICATE, Network.VISA),
    VISA_REJECTED("07", TransactionStatus.REJECTED, Network.VISA),

    MC_SUCCESS_PA("02", TransactionStatus.SUCCESS, Network.MASTERCARD, MessageCategory.PA),
    MC_SUCCESS_NPA("N2", TransactionStatus.SUCCESS, Network.MASTERCARD, MessageCategory.NPA),
    MC_SUCCESS_PVPA("02", TransactionStatus.SUCCESS, Network.MASTERCARD, MessageCategory.PVPA),
    MC_SUCCESS_PVNPA("N2", TransactionStatus.SUCCESS, Network.MASTERCARD, MessageCategory.PVNPA),
    MC_SUCCESS_RPA("07", TransactionStatus.SUCCESS, Network.MASTERCARD, MessageCategory.PA),
    MC_INFORMATIONAL("06", TransactionStatus.INFORMATIONAL, Network.MASTERCARD),

    MC_ATTEMPTS("01", TransactionStatus.ATTEMPT, Network.MASTERCARD),
    MC_FAILED_PA("00", TransactionStatus.FAILED, Network.MASTERCARD, MessageCategory.PA),
    MC_FAILED_NPA("N0", TransactionStatus.FAILED, Network.MASTERCARD, MessageCategory.NPA),
    MC_FAILED_PVPA("00", TransactionStatus.FAILED, Network.MASTERCARD, MessageCategory.PVPA),
    MC_FAILED_PVNPA("N0", TransactionStatus.FAILED, Network.MASTERCARD, MessageCategory.PVNPA),
    MC_REJECTED("00", TransactionStatus.REJECTED, Network.MASTERCARD),

    AMEX_SUCCESS("05", TransactionStatus.SUCCESS, Network.AMEX),
    AMEX_ATTEMPTS("06", TransactionStatus.ATTEMPT, Network.AMEX),
    AMEX_FAILED("07", TransactionStatus.FAILED, Network.AMEX),
    AMEX_UNAUTHORIZED("07", TransactionStatus.UNABLE_TO_AUTHENTICATE, Network.AMEX),

    DISCOVER_SUCCESS("05", TransactionStatus.SUCCESS, Network.DISCOVER),
    DISCOVER_ATTEMPTS("06", TransactionStatus.ATTEMPT, Network.DISCOVER),
    DISCOVER_FAILED("07", TransactionStatus.FAILED, Network.DISCOVER),
    DISCOVER_UNAUTHORIZED("07", TransactionStatus.UNABLE_TO_AUTHENTICATE, Network.DISCOVER),
    DISCOVER_REJECTED("07", TransactionStatus.REJECTED, Network.DISCOVER);

    ECI(String value, TransactionStatus status, Network network) {
        this.value = value;
        this.status = status;
        this.network = network;
    }

    ECI(String value, TransactionStatus status, Network network, MessageCategory messageCategory) {
        this.value = value;
        this.status = status;
        this.network = network;
        this.messageCategory = messageCategory;
    }

    private final String value;
    private final TransactionStatus status;
    private final Network network;
    private MessageCategory messageCategory;

    public static String getValue(TransactionStatus status, Network network) {
        String result = null;
        for (ECI eci : ECI.values()) {
            if (eci.network.equals(network) && eci.status.equals(status)) {
                result = eci.value;
                break;
            }
        }
        return result;
    }

    public static String getValue(
            TransactionStatus status, Network network, MessageCategory messageCategory) {
        String result = null;
        for (ECI eci : ECI.values()) {

            if (eci.messageCategory != null) {
                if (eci.network.equals(network)
                        && eci.status.equals(status)
                        && messageCategory.equals(eci.messageCategory)) {
                    result = eci.value;
                    break;
                }
            } else {
                if (eci.network.equals(network) && eci.status.equals(status)) {
                    result = eci.value;
                    break;
                }
            }
        }
        return result;
    }
}
