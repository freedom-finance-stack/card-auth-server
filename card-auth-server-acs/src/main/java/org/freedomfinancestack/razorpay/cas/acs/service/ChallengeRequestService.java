package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;

public interface ChallengeRequestService {
    public ChallengeFlowDto processChallengeRequest(
            DeviceChannel flowType, String strCReq, String threeDSSessionData)
            throws ThreeDSException, ACSDataAccessException;
}
