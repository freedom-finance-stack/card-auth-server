package org.freedomfinancestack.razorpay.cas.acs.service;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

/**
 * The {@code FeatureService} interface is responsible for fetching the authentication configuration
 * for a given set of card range, ground and institution ids. If config exist under multiple ids
 * provided it will give precedence in following order CardRange, CardRangeGroup, Institution.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 */
public interface FeatureService {
    AuthConfigDto getAuthenticationConfig(Map<FeatureEntityType, String> entityIdsByType)
            throws ACSDataAccessException;

    RenderingTypeConfig getRenderingTypeConfig(
            Transaction transaction, DeviceInterface deviceInterface, boolean fetchDefault)
            throws ACSDataAccessException;

    AuthConfigDto getAuthenticationConfig(Transaction transaction) throws ACSDataAccessException;
}
