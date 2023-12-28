package org.freedomfinancestack.razorpay.cas.acs.service;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.AuthConfigException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.DeviceRenderOptions;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
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
            throws AuthConfigException;

    void getACSRenderingType(Transaction transaction, DeviceRenderOptions deviceRenderOptions)
            throws ThreeDSException;

    AuthConfigDto getAuthenticationConfig(Transaction transaction) throws AuthConfigException;
}
