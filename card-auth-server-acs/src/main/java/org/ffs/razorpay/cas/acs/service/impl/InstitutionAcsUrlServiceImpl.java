package org.ffs.razorpay.cas.acs.service.impl;

import java.util.Optional;

import org.ffs.razorpay.cas.acs.exception.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.service.InstitutionAcsUrlService;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrlPK;
import org.ffs.razorpay.cas.dao.repository.InstitutionAcsUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionAcsUrlServiceImpl implements InstitutionAcsUrlService {
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
