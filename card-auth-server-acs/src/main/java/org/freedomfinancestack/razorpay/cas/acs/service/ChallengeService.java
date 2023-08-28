package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.contract.*;

public interface ChallengeService {

    ChallengeResponse processBrwChallengeRequest(
            final String creq, final String threeDSSessionData);

    ValidateChallengeResponse validateChallengeRequest(
            final ValidateChallengeRequest validateChallengeRequest);
}
