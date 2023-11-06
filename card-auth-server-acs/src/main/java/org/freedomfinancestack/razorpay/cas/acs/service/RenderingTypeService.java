package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfigPK;

public interface RenderingTypeService {

    RenderingTypeConfig findById(RenderingTypeConfigPK renderingTypeConfigPK)
            throws ACSDataAccessException, DataNotFoundException;

    RenderingTypeConfig findRenderingType(
            String institutionId, String cardRangeId, String acsInterface)
            throws ACSDataAccessException, DataNotFoundException;

    RenderingTypeConfig findDefaultRenderingType(
            String institutionId, String cardRangeId, String acsInterface)
            throws ACSDataAccessException, DataNotFoundException;
}
