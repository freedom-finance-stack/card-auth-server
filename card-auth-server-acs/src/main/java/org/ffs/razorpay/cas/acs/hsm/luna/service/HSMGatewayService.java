package org.ffs.razorpay.cas.acs.hsm.luna.service;

import org.ffs.razorpay.cas.acs.exception.HSMConnectionException;

public interface HSMGatewayService<H, M> {

    public void sendRequest(H handler, M message) throws HSMConnectionException;

    public byte[] getResponse(Object correlationKey) throws HSMConnectionException;
}
