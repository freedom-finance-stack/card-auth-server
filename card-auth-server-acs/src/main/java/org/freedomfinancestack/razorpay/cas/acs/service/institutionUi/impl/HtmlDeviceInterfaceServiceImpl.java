package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.DeviceInterfaceService;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "htmlDeviceInterfaceServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HtmlDeviceInterfaceServiceImpl implements DeviceInterfaceService {

    @Override
    public void populateInstitutionUiConfig(Transaction transaction, AppChallengeFlowDto challengeFlowDto, InstitutionUiConfig institutionUiConfig) throws ACSDataAccessException {

    }
}
