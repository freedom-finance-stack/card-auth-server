package com.razorpay.ffs.cas.acs.service;

import com.razorpay.ffs.cas.acs.exception.DataNotFoundException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.dao.model.InstitutionAcsUrl;
import com.razorpay.ffs.cas.dao.model.InstitutionAcsUrlPK;

public interface InstitutionAcsUrlService {
    InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException;
}
