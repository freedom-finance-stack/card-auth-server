package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.contract.*;

public interface ChallengeRequestService {

    CdRes processBrwChallengeRequest(final String creq, final String threeDSSessionData);

    CdRes processBrwChallengeValidationRequest(final CVReq CVReq);
}
