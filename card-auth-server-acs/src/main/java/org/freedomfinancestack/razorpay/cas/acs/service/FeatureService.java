package org.freedomfinancestack.razorpay.cas.acs.service;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;

public interface FeatureService {
    AuthConfigDto getAuthenticationConfig(Map<FeatureEntityType, String> entityIdsByType)
            throws ACSDataAccessException;
}
