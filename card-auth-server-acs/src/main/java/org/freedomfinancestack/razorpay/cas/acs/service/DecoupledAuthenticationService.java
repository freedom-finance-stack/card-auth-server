package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface DecoupledAuthenticationService {

    DecoupledAuthenticationResponse processAuthenticationRequest(
            final Transaction transaction,
            final DecoupledAuthenticationRequest decoupledAuthenticationRequest)
            throws ThreeDSException, ACSDataAccessException;
}
