package org.ffs.razorpay.cas.contract.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EMVCOConstant {

    /* All constants are defined in EMVCo specification documents.
     * EMVCo_3DS_Spec_210_1017_0318.pdf
     * */
    public static final String MESSAGE_TYPE_ARES = "ARes";
    public static final String MESSAGE_TYPE_CREQ = "CReq";
    public static final String MESSAGE_TYPE_ERRO = "Erro";
    public static final String MESSAGE_TYPE_VERSION = "2.2.0";
    public static final String appDeviceInfoAndroid = "Android";
    public static final String appDeviceInfoIOS = "iOS";

    public static final List<String> excludedCountry =
            new ArrayList<String>(
                    Arrays.asList(
                            "955", "956", "957", "958", "959", "960", "961", "962", "963", "964",
                            "999", "901"));

    public static final List<String> chAccAgeIndList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05"));

    public static final List<String> chAccChangeIndList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04"));

    public static final List<String> chAccPwChangeIndList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05"));

    public static final List<String> paymentAccIndList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05"));

    public static final List<String> shipAddressUsageIndList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04"));

    public static final List<String> shipNameIndicatorList =
            new ArrayList<String>(Arrays.asList("01", "02"));

    public static final List<String> suspiciousAccActivityList =
            new ArrayList<String>(Arrays.asList("01", "02"));

    public static final List<String> sdkMaxTimeoutList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04"));

    public static final List<String> threeDSReqPriorAuthMethodList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04"));

    public static final List<String> deliveryTimeframeList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04"));

    public static final List<String> preOrderPurchaseIndList =
            new ArrayList<String>(Arrays.asList("01", "02"));

    public static final List<String> reorderItemsIndList =
            new ArrayList<String>(Arrays.asList("01", "02"));

    public static final List<String> shipIndicatorList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05", "06", "07"));

    public static final List<String> threeDSReqAuthMethodList =
            new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05", "06"));

    public static final List<String> accType =
            new ArrayList<String>(Arrays.asList("01", "02", "03"));
}
