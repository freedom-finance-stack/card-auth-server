package com.razorpay.ffs.cas.acs.service;

import com.razorpay.ffs.cas.acs.exception.ThreeDSException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.contract.AREQ;
import com.razorpay.ffs.cas.contract.ARES;

public interface AuthenticationService {

    ARES processAuthenticationRequest(final AREQ areq)
            throws ThreeDSException, ACSDataAccessException;
}
