package com.razorpay.ffs.cas.acs.hsm.luna.service;

import com.razorpay.ffs.cas.acs.exception.HSMConnectionException;

public interface HSMGatewayService<H, M> {

    public void sendRequest(H handler, M message) throws HSMConnectionException;

    public byte[] getResponse(Object correlationKey) throws HSMConnectionException;
}
