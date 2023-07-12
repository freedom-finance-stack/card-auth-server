package com.razorpay.threeds.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;
import com.razorpay.acs.dao.repository.InstitutionAcsUrlRepository;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.InternalErrorCode;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionAcsUrlServiceImpl
        implements com.razorpay.threeds.service.InstitutionAcsUrlService {
    private final InstitutionAcsUrlRepository institutionAcsUrlRepository;

    @Override
    public InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException {
        try {
            Optional<InstitutionAcsUrl> acsUrl =
                    institutionAcsUrlRepository.findById(
                            institutionAcsUrlPK); // DB connection, DB query, DB contraint, DB
            // transaction
            if (acsUrl.isPresent()) {
                return acsUrl.get();
            }
        } catch (DataAccessException e) {
            log.error(
                    "Error while fetching acs url for Institution ID :"
                            + institutionAcsUrlPK.getInstitutionId()
                            + " and Network: "
                            + institutionAcsUrlPK.getNetworkCode()
                            + " and Channel: "
                            + institutionAcsUrlPK.getDeviceChannel());
            throw new ACSDataAccessException(InternalErrorCode.INSTITUTION_FETCH_EXCEPTION, e);
        }

        log.error(
                "Acs url not found for Institution ID :"
                        + institutionAcsUrlPK.getInstitutionId()
                        + " and Network: "
                        + institutionAcsUrlPK.getNetworkCode()
                        + " and Channel: "
                        + institutionAcsUrlPK.getDeviceChannel());
        throw new DataNotFoundException(
                ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                InternalErrorCode.ACS_URL_NOT_FOUND);
    }
}
