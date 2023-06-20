package com.razorpay.threeds.service;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;

public interface AuthenticationService {

  ARES processAuthenticationRequest(final AREQ areq);
}
