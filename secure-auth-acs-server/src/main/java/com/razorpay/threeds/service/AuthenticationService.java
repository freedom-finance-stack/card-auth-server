package com.razorpay.threeds.service;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;

import lombok.NonNull;

public interface AuthenticationService {

  ARES processAuthenticationRequest(@NonNull final AREQ areq);
}
