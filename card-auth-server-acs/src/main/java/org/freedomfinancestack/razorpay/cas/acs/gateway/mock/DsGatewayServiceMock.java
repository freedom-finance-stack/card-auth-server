package org.freedomfinancestack.razorpay.cas.acs.gateway.mock;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ds.DsGatewayService;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service("mockDsService")
@Slf4j
public class DsGatewayServiceMock implements DsGatewayService {

    @Override
    public RRES sendRReq(Network network, RREQ rReq) throws ACSValidationException {
        log.info("sendRReq of mocked DS called");
        RRES rres = new RRES();
        rres.setAcsTransID(rReq.getAcsTransID());
        rres.setMessageType(MessageType.RRes.toString());
        rres.setDsTransID("DSTRANSID");
        rres.setThreeDSServerTransID(rReq.getThreeDSServerTransID());
        rres.setMessageVersion(rReq.getMessageVersion());
        rres.setResultsStatus("01");
        return rres;
    }

    @Override
    public void sendError(Network network, ThreeDSErrorResponse errorResponse)
            throws ACSValidationException {
        log.info("sendError of mocked DS called");
    }
}
