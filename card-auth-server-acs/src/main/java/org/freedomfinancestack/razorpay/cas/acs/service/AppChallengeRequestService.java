package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;

public interface AppChallengeRequestService {
    String processAppChallengeRequest(String strCReq)
            throws ThreeDSException, ACSDataAccessException;

    String processAppChallengeRequestHandler(String strCReq)
            throws ThreeDSException, ACSDataAccessException, InvalidStateTransactionException;
}
