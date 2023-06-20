package com.razorpay.acs.dao.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.razorpay.acs.dao.contract.constants.EMVCOConstant;
import com.razorpay.acs.dao.contract.utils.Util;

import lombok.Data;

@Data
public class ThreeDSMerchantFeilds {

  @JsonProperty("shipIndicator")
  private String shipIndicator;

  @JsonProperty("deliveryTimeframe")
  private String deliveryTimeframe;

  @JsonProperty("deliveryEmailAddress")
  private String deliveryEmailAddress;

  @JsonProperty("reorderItemsInd")
  private String reorderItemsInd;

  @JsonProperty("preOrderPurchaseInd")
  private String preOrderPurchaseInd;

  @JsonProperty("preOrderDate")
  private String preOrderDate;

  @JsonProperty("giftCardAmount")
  private String giftCardAmount;

  @JsonProperty("giftCardCurr")
  private String giftCardCurr;

  @JsonProperty("giftCardCount")
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
    if (this.deliveryEmailAddress != null) {
      if (deliveryEmailAddress.length() < 1 && deliveryEmailAddress.length() > 254) {
        return false;
      }
    }

    if (this.deliveryTimeframe != null) {
      if (!EMVCOConstant.deliveryTimeframeList.contains(deliveryTimeframe)) {
        return false;
      }
    }

    if (this.giftCardAmount != null) {
      if (giftCardAmount.length() < 1 && giftCardAmount.length() > 15) {
        return false;
      }
    }

    if (this.giftCardCount != null) {
      if (giftCardCount.length() < 1 && giftCardCount.length() > 2) {
        return false;
      }
    }

    if (this.giftCardCurr != null) {
      if (giftCardCurr.length() < 1 && giftCardCurr.length() > 3) {
        return false;
      } else if (EMVCOConstant.excludedCurrency.contains(this.giftCardCurr)) {
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
      if (!EMVCOConstant.shipIndicatorList.contains(shipIndicator)) {
        return false;
      }
    }

    return true;
  }
}
