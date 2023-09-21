package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.InvalidStateTransactionException;

public interface ChallengeRequestService {

    CdRes processBrwChallengeRequest(final String creq, final String threeDSSessionData)
            throws ACSDataAccessException, InvalidStateTransactionException;

    CdRes processBrwChallengeValidationRequest(final CVReq CVReq);
}
