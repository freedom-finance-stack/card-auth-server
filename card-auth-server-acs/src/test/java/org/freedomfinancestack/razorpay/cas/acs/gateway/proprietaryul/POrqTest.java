package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class POrqTest {
    @Test
    public void test_valid_input_parameters() {
        // give me test cases for more and wider test coverage
        POrq porq =
                new POrq(
                        "messageType",
                        "acsTransID",
                        "threeDSServerTransID",
                        "messageVersion",
                        "p_messageVersion");
        assertEquals("messageType", porq.getMessageType());
        assertEquals("acsTransID", porq.getAcsTransID());
        assertEquals("threeDSServerTransID", porq.getThreeDSServerTransID());
        assertEquals("messageVersion", porq.getMessageVersion());
        assertEquals("p_messageVersion", porq.getP_messageVersion());
    }
}
