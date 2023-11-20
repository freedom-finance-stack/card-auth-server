package org.freedomfinancestack.razorpay.cas.acs.service;

import lombok.NonNull;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.CRES;

public interface AppChallengeRequestService {
    CRES processAppChallengeRequest(@NonNull CREQ creq) throws ThreeDSException, ACSDataAccessException;
}
