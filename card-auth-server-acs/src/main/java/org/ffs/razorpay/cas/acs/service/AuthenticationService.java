package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.ThreeDSException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.contract.ARES;

public interface AuthenticationService {

    ARES processAuthenticationRequest(final AREQ areq)
            throws ThreeDSException, ACSDataAccessException;
}
