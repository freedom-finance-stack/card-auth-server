package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.contract.*;

public interface ChallengeRequestService {

    ChallengeResponse processBrwChallengeRequest(
            final String CReq, final String threeDSSessionData);

    ValidateChallengeResponse processBrwChallengeValidationRequest(final CVReq CVReq);
}
