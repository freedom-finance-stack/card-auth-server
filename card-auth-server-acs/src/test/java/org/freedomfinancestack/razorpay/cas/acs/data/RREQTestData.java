package org.freedomfinancestack.razorpay.cas.acs.data;

import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;

import static org.freedomfinancestack.razorpay.cas.acs.data.TestUtil.replaceData;

public class RREQTestData {
    String validRReq =
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
    String validRRes =
            "{\n"
                + "        \"threeDSServerTransID\": \"b3c9516d-47ab-41b5-8e6e-401d3cae691a\",\n"
                + "        \"acsTransID\": \"d8ac9cd2-e72f-4918-93d0-24a684826394\",\n"
                + "        \"dsTransID\": \"f52253dd-3a06-4fbc-afe7-688e426dd26c\",\n"
                + "        \"messageType\": \"RRes\",\n"
                + "        \"messageVersion\": \"2.2.0\",\n"
                + "        \"resultsStatus\": \"01\"\n"
                + "      }";

    static Map<String, Object> RREQ_TEST_DATA = new HashMap<>();
    static Map<String, Object> RRES_TEST_DATA = new HashMap<>();

    public RREQ getValidRReq() {
        return Util.fromJson(validRReq, RREQ.class);
    }

    public RRES getValidRRes() {
        return Util.fromJson(validRRes, RRES.class);
    }

    // data = {"threeDSServerTransID" : "INVALID1_THREEDSSERVERTRANSID"} represent {fieldName :
    // MockData refer}
    public RREQ getRReq(HashMap<String, String> data) {
        return replaceData(getValidRReq(), data, RREQ_TEST_DATA);
    }

    public RRES getRRes(HashMap<String, String> data) {
        return replaceData(getValidRRes(), data, RRES_TEST_DATA);
    }
}
