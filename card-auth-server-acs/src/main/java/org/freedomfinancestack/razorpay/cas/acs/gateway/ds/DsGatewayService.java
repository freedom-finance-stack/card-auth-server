package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;

public interface DsGatewayService {
    RRES sendRReq(final Network network, final RREQ rReq) throws ValidationException;

    void sendError(final Network network, final ThreeDSErrorResponse errorResponse)
            throws ValidationException;
}
