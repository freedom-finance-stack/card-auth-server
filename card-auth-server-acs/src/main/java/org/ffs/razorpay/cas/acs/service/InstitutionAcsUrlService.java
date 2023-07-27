package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrlPK;

public interface InstitutionAcsUrlService {
    InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException;
}
