package org.freedomfinancestack.razorpay.cas.acs.gateway.mock;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.RREQTestData.getValidRReq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DsGatewayServiceMockTest {
    RREQ rReq = getValidRReq();
    @InjectMocks DsGatewayServiceMock dsGatewayServiceMock;

    @Test
    void sendRReq() throws ACSValidationException {
        RRES rres = dsGatewayServiceMock.sendRReq(mock(Network.class), rReq);

        assertNotNull(rres);
        assertEquals(rReq.getAcsTransID(), rres.getAcsTransID());
        assertEquals(MessageType.RRes.toString(), rres.getMessageType());
        assertEquals(rReq.getDsTransID(), rres.getDsTransID());
        assertEquals(rReq.getThreeDSServerTransID(), rres.getThreeDSServerTransID());
        assertEquals(rReq.getMessageVersion(), rres.getMessageVersion());
        assertEquals("01", rres.getResultsStatus());
    }

    @Test
    public void sendError() throws ACSValidationException {
        dsGatewayServiceMock.sendError(Network.VISA, new ThreeDSErrorResponse());
    }
}
