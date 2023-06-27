package com.razorpay.threeds.hsm.luna.service;

import com.razorpay.threeds.exception.checked.ACSException;

public interface HSMGatewayService<H, M> {

  public void sendRequest(H handler, M message) throws ACSException;

  public byte[] getResponse(Object correlationKey) throws ACSException;
}
