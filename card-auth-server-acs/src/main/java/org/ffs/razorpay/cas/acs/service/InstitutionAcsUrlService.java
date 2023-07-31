package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrlPK;

public interface InstitutionAcsUrlService {
    InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException;
}
