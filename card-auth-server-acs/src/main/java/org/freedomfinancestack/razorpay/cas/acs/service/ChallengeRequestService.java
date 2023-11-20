package org.freedomfinancestack.razorpay.cas.acs.service;

import lombok.NonNull;
import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.*;

/**
 * The {@code ChallengeRequestService} interface represents a service responsible for processing
 * challenge request (CReq) and challenge validation request (CVReq) and generating challenge
 * response (CRes) for 3D Secure transactions. The service handles the validation of CReq,
 * communication with the DS (Directory Service), and generation of the appropriate CRes based on
 * the transaction status and other parameters.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ChallengeRequestService {

    /**
     * Process the challenge request (CReq) and generate the challenge display response (CDRes) for
     * the browser based request
     *
     * @param creq challenge request
     * @param threeDSSessionData
     * @return Cdres challenge display response
     */
    CdRes processBrwChallengeRequest(final String creq, final String threeDSSessionData);

    /**
     * Process the challenge validation request (CVReq) and generate the challenge display response
     * (CDRes) for the browser based request
     *
     * @param CVReq
     * @return
     */
    CdRes processBrwChallengeValidationRequest(final CVReq CVReq);
}
