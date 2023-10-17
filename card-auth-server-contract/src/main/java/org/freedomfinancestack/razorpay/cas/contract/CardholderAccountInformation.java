package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.extensions.validation.validator.Validatable;
import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.utils.Util;

public class CardholderAccountInformation implements Validatable {

    private String chAccAgeInd;

    private String chAccDate;

    private String chAccChangeInd;

    private String chAccChange;

    private String chAccPwChangeInd;

    private String chAccPwChange;

    private String shipAddressUsageInd;

    private String shipAddressUsage;

    private String txnActivityDay;

    private String txnActivityYear;

    private String provisionAttemptsDay;

    private String nbPurchaseAccount;

    private String suspiciousAccActivity;

    private String shipNameIndicator;

    private String paymentAccInd;

    private String paymentAccAge;

    public int getLength() {
        int counter = 0;

        if (this.chAccAgeInd != null) {
            counter++;
        }
        if (this.chAccChange != null) {
            counter++;
        }
        if (this.chAccChangeInd != null) {
            counter++;
        }
        if (this.chAccDate != null) {
            counter++;
        }
        if (this.chAccPwChange != null) {
            counter++;
        }
        if (this.chAccPwChangeInd != null) {
            counter++;
        }
        if (this.nbPurchaseAccount != null) {
            counter++;
        }
        if (this.paymentAccAge != null) {
            counter++;
        }
        if (this.paymentAccInd != null) {
            counter++;
        }
        if (this.provisionAttemptsDay != null) {
            counter++;
        }
        if (this.shipAddressUsage != null) {
            counter++;
        }
        if (this.shipAddressUsageInd != null) {
            counter++;
        }
        if (this.shipNameIndicator != null) {
            counter++;
        }
        if (this.suspiciousAccActivity != null) {
            counter++;
        }
        if (this.txnActivityDay != null) {
            counter++;
        }
        if (this.txnActivityYear != null) {
            counter++;
        }

        return counter;
    }

    public boolean isValid() {

        if (this.getLength() == 0) {
            return false;
        }

        if (!this.isDataValid()) {
            return false;
        }
        if (this.chAccDate != null) {
            if (!Util.isValidDate(chAccDate, Util.DATE_FORMAT_YYYYMMDD)) {
                return false;
            }
        }

        if (this.chAccChange != null) {
            if (!Util.isValidDate(chAccChange, Util.DATE_FORMAT_YYYYMMDD)) {
                return false;
            }
        }

        if (this.chAccPwChange != null) {
            if (!Util.isValidDate(chAccPwChange, Util.DATE_FORMAT_YYYYMMDD)) {
                return false;
            }
        }

        if (this.paymentAccAge != null) {
            if (!Util.isValidDate(paymentAccAge, Util.DATE_FORMAT_YYYYMMDD)) {
                return false;
            }
        }

        if (this.shipAddressUsage != null) {
            if (!Util.isValidDate(shipAddressUsage, Util.DATE_FORMAT_YYYYMMDD)) {
                return false;
            }
        }

        if (this.suspiciousAccActivity != null) {
            return this.suspiciousAccActivity.length() <= 2;
        }

        return true;
    }

    public boolean isDataValid() {
        if (this.chAccAgeInd != null) {
            if (!EMVCOConstant.chAccAgeIndList.contains(chAccAgeInd)) {
                return false;
            }
        }

        if (this.chAccChangeInd != null) {
            if (!EMVCOConstant.chAccChangeIndList.contains(chAccChangeInd)) {
                return false;
            }
        }

        if (this.chAccPwChangeInd != null) {
            if (!EMVCOConstant.chAccPwChangeIndList.contains(chAccPwChangeInd)) {
                return false;
            }
        }

        if (this.paymentAccInd != null) {
            if (!EMVCOConstant.paymentAccIndList.contains(paymentAccInd)) {
                return false;
            }
        }

        if (this.shipAddressUsageInd != null) {
            if (!EMVCOConstant.shipAddressUsageIndList.contains(shipAddressUsageInd)) {
                return false;
            }
        }

        if (this.shipNameIndicator != null) {
            if (!EMVCOConstant.shipNameIndicatorList.contains(shipNameIndicator)) {
                return false;
            }
        }

        if (this.suspiciousAccActivity != null) {
            return EMVCOConstant.suspiciousAccActivityList.contains(suspiciousAccActivity);
        }

        return true;
    }

    public boolean isEmpty() {
        return this.chAccAgeInd == null
                && this.chAccChangeInd == null
                && this.chAccPwChangeInd == null
                && this.paymentAccInd == null
                && this.shipAddressUsageInd == null
                && this.shipNameIndicator == null
                && this.suspiciousAccActivity == null;
    }
}
