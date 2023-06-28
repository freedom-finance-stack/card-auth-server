package com.razorpay.threeds.hsm.luna.service;

import com.razorpay.threeds.exception.HSMConnectionException;

public interface HSMGatewayService<H, M> {

  public void sendRequest(H handler, M message) throws HSMConnectionException;

  public byte[] getResponse(Object correlationKey) throws HSMConnectionException;
}
