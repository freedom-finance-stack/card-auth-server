package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class POrsTest {

    @Test
    public void test_create_with_valid_input_parameters() {
        // give me test cases for more and wider test coverage
        POrs pOrs = new POrs("messageType", true, "messageVersion", "p_messageVersion");

        assertEquals("messageType", pOrs.getMessageType());
        assertTrue(pOrs.isP_isOobSuccessful());
        assertEquals("messageVersion", pOrs.getMessageVersion());
        assertEquals("p_messageVersion", pOrs.getP_messageVersion());
    }

}