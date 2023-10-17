package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.extensions.validation.validator.Validatable;
import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.utils.Util;

import lombok.Data;

@Data
public class ThreeDSMerchantFeilds implements Validatable {

    private String shipIndicator;

    private String deliveryTimeframe;

    private String deliveryEmailAddress;

    private String reorderItemsInd;

    private String preOrderPurchaseInd;

    private String preOrderDate;

    private String giftCardAmount;

    private String giftCardCurr;

    private String giftCardCount;

    public int getLength() {
        int counter = 0;

        if (this.deliveryEmailAddress != null) {
            counter++;
        }
        if (this.deliveryTimeframe != null) {
            counter++;
        }
        if (this.giftCardAmount != null) {
            counter++;
        }
        if (this.giftCardCount != null) {
            counter++;
        }
        if (this.giftCardCurr != null) {
            counter++;
        }
        if (this.preOrderDate != null) {
            counter++;
        }
        if (this.preOrderPurchaseInd != null) {
            counter++;
        }
        if (this.reorderItemsInd != null) {
            counter++;
        }
        if (this.shipIndicator != null) {
            counter++;
        }
        return counter;
    }

    public boolean isValid() {

        if (this.getLength() == 0) {
            return false;
        }

        if (this.deliveryEmailAddress != null
                && (deliveryEmailAddress.length() < 1 || deliveryEmailAddress.length() > 254)) {
            return false;
        }

        if (this.deliveryTimeframe != null) {
            if (!EMVCOConstant.deliveryTimeframeList.contains(deliveryTimeframe)) {
                return false;
            }
        }

        if (this.giftCardAmount != null
                && (giftCardAmount.length() < 1 || giftCardAmount.length() > 15)) {
            return false;
        }

        if (this.giftCardCount != null
                && (giftCardCount.length() < 1 || giftCardCount.length() > 2)) {
            return false;
        }

        if (this.giftCardCurr != null) {
            if (giftCardCurr.length() < 1 || giftCardCurr.length() > 3) {
                return false;
            } else if (EMVCOConstant.excludedCountry.contains(this.giftCardCurr)) {
                return false;
            } else if (!giftCardCurr.matches("[0-9]{3}")) {
                return false;
            }
        }

        if (this.preOrderDate != null) {
            if (!Util.isValidDate(preOrderDate, Util.DATE_FORMAT_YYYYMMDD)) {
                return false;
            }
        }

        if (this.preOrderPurchaseInd != null) {
            if (!EMVCOConstant.preOrderPurchaseIndList.contains(preOrderPurchaseInd)) {
                return false;
            }
        }

        if (this.reorderItemsInd != null) {
            if (!EMVCOConstant.reorderItemsIndList.contains(reorderItemsInd)) {
                return false;
            }
        }

        if (this.shipIndicator != null) {
            return EMVCOConstant.shipIndicatorList.contains(shipIndicator);
        }

        return true;
    }
}
