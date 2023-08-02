package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrlPK;

/**
 * The {@code InstitutionAcsUrlService} interface provides methods to retrieve InstitutionAcsUrl
 * entities based on their primary key (InstitutionAcsUrlPK). The service is responsible for
 * querying the data source to fetch the corresponding InstitutionAcsUrl entity based on the given
 * primary key.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface InstitutionAcsUrlService {

    /**
     * Finds and retrieves the InstitutionAcsUrl entity based on the given primary key
     * (InstitutionAcsUrlPK).
     *
     * @param institutionAcsUrlPK The {@link InstitutionAcsUrlPK} object representing the primary
     *     key of the InstitutionAcsUrl entity to be fetched.
     * @return The {@link InstitutionAcsUrl} entity corresponding to the given primary key.
     * @throws ACSDataAccessException If there is an exception specific to the ACS (Access Control
     *     Server) functionality while fetching the entity.
     * @throws DataNotFoundException If the InstitutionAcsUrl entity with the given primary key is
     *     not found in the data source.
     */
    InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException;
}
