package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.service.InstitutionAcsUrlService;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionAcsUrlPK;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionAcsUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code InstitutionAcsUrlServiceImpl} class is the implementation of {@link
 * InstitutionAcsUrlService}. The service is responsible for querying the data source to fetch the
 * corresponding InstitutionAcsUrl entity based on the given primary key.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionAcsUrlServiceImpl implements InstitutionAcsUrlService {
    private final InstitutionAcsUrlRepository institutionAcsUrlRepository;

    /**
     * @param institutionAcsUrlPK The {@link InstitutionAcsUrlPK} object representing the primary
     *     key of the InstitutionAcsUrl entity to be fetched.
     * @return InstitutionAcsUrl
     * @throws ACSDataAccessException
     * @throws DataNotFoundException
     */
    @Override
    public InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException {
        try {
            Optional<InstitutionAcsUrl> acsUrl =
                    institutionAcsUrlRepository.findById(institutionAcsUrlPK);
            // transaction
            if (acsUrl.isPresent()) {
                return acsUrl.get();
            }
        } catch (DataAccessException e) {
            log.error(
                    "Error while fetching acs url for Institution ID : "
                            + institutionAcsUrlPK.getInstitutionId()
                            + " and Network: "
                            + institutionAcsUrlPK.getNetworkCode()
                            + " and Channel: "
                            + institutionAcsUrlPK.getDeviceChannel());
            throw new ACSDataAccessException(InternalErrorCode.INSTITUTION_FETCH_EXCEPTION, e);
        }

        log.error(
                "Acs url not found for Institution ID : "
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
