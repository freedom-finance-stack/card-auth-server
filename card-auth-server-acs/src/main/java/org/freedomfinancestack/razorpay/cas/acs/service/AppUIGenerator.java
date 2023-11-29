package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface AppUIGenerator {

    void generateAppUIParams(
            AppChallengeFlowDto challengeFlowDto,
            Transaction transaction,
            AuthConfigDto authConfigDto)
            throws ACSDataAccessException;
}
