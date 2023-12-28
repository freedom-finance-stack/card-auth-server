package org.freedomfinancestack.razorpay.cas.acs.data;

import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;

import static org.freedomfinancestack.razorpay.cas.acs.data.TestUtil.replaceData;

public class RREQTestData {
    static String validRReq =
            "{\n"
                + "        \"threeDSServerTransID\": \"b3c9516d-47ab-41b5-8e6e-401d3cae691a\",\n"
                + "        \"acsTransID\": \"d8ac9cd2-e72f-4918-93d0-24a684826394\",\n"
                + "        \"dsTransID\": \"f52253dd-3a06-4fbc-afe7-688e426dd26c\",\n"
                + "        \"interactionCounter\": \"00\",\n"
                + "        \"messageCategory\": \"01\",\n"
                + "        \"messageType\": \"RReq\",\n"
                + "        \"messageVersion\": \"2.2.0\",\n"
                + "        \"transStatus\": \"U\",\n"
                + "        \"authenticationMethod\": \"02\",\n"
                + "        \"authenticationType\": \"02\",\n"
                + "        \"challengeCancel\": \"06\",\n"
                + "        \"eci\": \"07\",\n"
                + "        \"transStatusReason\": \"07\"\n"
                + "      }";
    static String validRRes =
            "{\n"
                + "        \"threeDSServerTransID\": \"b3c9516d-47ab-41b5-8e6e-401d3cae691a\",\n"
                + "        \"acsTransID\": \"d8ac9cd2-e72f-4918-93d0-24a684826394\",\n"
                + "        \"dsTransID\": \"f52253dd-3a06-4fbc-afe7-688e426dd26c\",\n"
                + "        \"messageType\": \"RRes\",\n"
                + "        \"messageVersion\": \"2.2.0\",\n"
                + "        \"resultsStatus\": \"01\"\n"
                + "      }";


    public static RREQ getValidRReq() {
        return Util.fromJson(validRReq, RREQ.class);
    }

    public static RRES getValidRRes() {
        return Util.fromJson(validRRes, RRES.class);
    }

    // data = {"threeDSServerTransID" : "INVALID1_THREEDSSERVERTRANSID"} represent {fieldName :
    // MockData refer}
    public static RREQ getRReq(Map<String, Object> rreqTestData ) {
        return replaceData(getValidRReq(), rreqTestData);
    }

    public static RRES getRRes(Map<String, Object> rresTestData) {
        return replaceData(getValidRRes(), rresTestData);
    }
}
