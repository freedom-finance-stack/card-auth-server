package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.UiConfigException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface AppUIGenerator {

    void generateAppUIParams(
            ChallengeFlowDto challengeFlowDto, Transaction transaction, AuthConfigDto authConfigDto)
            throws UiConfigException;
}
