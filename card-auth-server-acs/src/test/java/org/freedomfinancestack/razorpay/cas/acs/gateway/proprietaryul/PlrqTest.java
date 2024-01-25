package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlrqTest {
    @Test
    public void test_new_instance_with_all_fields_initialized() {
        Plrq plrq = new Plrq();
        plrq.setMessageType("messageType");
        plrq.setAcsTransID("acsTransID");
        plrq.setMessageVersion("messageVersion");
        plrq.setP_messageVersion("p_messageVersion");
        Plrq.PFormValuesBRW pFormValuesBRW = new Plrq.PFormValuesBRW();
        pFormValuesBRW.setAction("action");
        pFormValuesBRW.setCorrectFormData("correctFormData");
        pFormValuesBRW.setIncorrectFormData("incorrectFormData");
        pFormValuesBRW.setCancelFormData("cancelFormData");
        plrq.setP_formValues_BRW(pFormValuesBRW);
        Plrq.PFormValuesAPP pFormValuesAPP = new Plrq.PFormValuesAPP();
        pFormValuesAPP.setCorrectChallengeData("correctChallengeData");
        pFormValuesAPP.setIncorrectChallengeData("incorrectChallengeData");
        pFormValuesAPP.setCorrectChallengeData2("correctChallengeData2");
        pFormValuesAPP.setIncorrectChallengeData2("incorrectChallengeData2");
        pFormValuesAPP.setCorrectInfoContinueHTML("correctInfoContinueHTML");
        plrq.setP_formValues_APP(pFormValuesAPP);

        assertEquals("messageType", plrq.getMessageType());
        assertEquals("acsTransID", plrq.getAcsTransID());
        assertEquals("messageVersion", plrq.getMessageVersion());
        assertEquals("p_messageVersion", plrq.getP_messageVersion());
        assertEquals("action", plrq.getP_formValues_BRW().getAction());
        assertEquals("correctFormData", plrq.getP_formValues_BRW().getCorrectFormData());
        assertEquals("incorrectFormData", plrq.getP_formValues_BRW().getIncorrectFormData());
        assertEquals("cancelFormData", plrq.getP_formValues_BRW().getCancelFormData());
        assertEquals("correctChallengeData", plrq.getP_formValues_APP().getCorrectChallengeData());
        assertEquals("incorrectChallengeData", plrq.getP_formValues_APP().getIncorrectChallengeData());
        assertEquals("correctChallengeData2", plrq.getP_formValues_APP().getCorrectChallengeData2());
        assertEquals("incorrectChallengeData2", plrq.getP_formValues_APP().getIncorrectChallengeData2());
        assertEquals("correctInfoContinueHTML", plrq.getP_formValues_APP().getCorrectInfoContinueHTML());
    }

}