package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;

/**
 * The {@code ChallengeRequestService} interface represents a service responsible for processing
 * challenge request (CReq) and generating challenge response (CRes) for 3D Secure transactions. The
 * service handles the validation of CReq, communication with the DS (Directory Service), and
 * generation of the appropriate CRes based on the transaction status and other parameters.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ChallengeRequestService {

    /**
     * Process the challenge request (CReq) and generate the challenge display response (CRes) for
     * the browser based and app based request
     *
     * @param flowType device channel flow app/brw
     * @param strCReq challenge request in serialized/encrypted format
     * @param threeDSSessionData session data from requester
     * @return ChallengeFlowDto challenge response dto
     */
    ChallengeFlowDto processChallengeRequest(
            DeviceChannel flowType, String strCReq, String threeDSSessionData)
            throws ThreeDSException, ACSDataAccessException;
}
