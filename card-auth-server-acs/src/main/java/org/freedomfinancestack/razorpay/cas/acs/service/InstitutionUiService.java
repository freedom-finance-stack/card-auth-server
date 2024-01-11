package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.UiConfigException;

public interface InstitutionUiService {

    void populateUiParams(ChallengeFlowDto challengeFlowDto, AuthConfigDto authConfigDto)
            throws UiConfigException;
}
