package com.razorpay.threeds.service;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;

public interface AuthenticationService {

  ARES processAuthenticationRequest(final AREQ areq) throws ThreeDSException, ACSDataAccessException;
}
