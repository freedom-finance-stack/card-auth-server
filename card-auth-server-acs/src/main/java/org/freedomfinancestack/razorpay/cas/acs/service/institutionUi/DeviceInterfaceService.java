package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.UiConfigException;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface DeviceInterfaceService {
    void generateUIParams(
            Transaction transaction,
            ChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto)
            throws UiConfigException;
}
