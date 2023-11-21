package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface InstitutionUiService {

    void populateInstitutionUiConfig(AppChallengeFlowDto challengeFlowDto, Transaction transaction)
            throws ACSDataAccessException;
}
