package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;

/**
 * The {@code AuthenticationService} interface represents a service responsible for processing
 * authentication requests (AReq) and generating authentication response (ARes) for 3D Secure
 * transactions. The service handles the validation of AReq, communication with the ACS (Access
 * Control Server), and generation of the appropriate ARes based on the transaction status and other
 * parameters.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ankitchoudhary2209
 */
public interface AuthenticationRequestService {

    /**
     * Processes the authentication request (AReq) and generates the corresponding authentication
     * response (ARes).
     *
     * @param areq The {@link AREQ} object representing the incoming authentication request (AReq).
     * @return The {@link ARES} object representing the authentication response (ARes) for the given
     *     AReq.
     * @throws ThreeDSException If any error occurs during the processing of the AReq, and the
     *     response needs to be sent as "Erro" message type.
     * @throws ACSDataAccessException If there is an exception specific to the ACS (Access Control
     *     Server) functionality that does not require sending "Erro" message type as the response.
     */
    ARES processAuthenticationRequest(final AREQ areq)
            throws ThreeDSException, ACSDataAccessException;
}
