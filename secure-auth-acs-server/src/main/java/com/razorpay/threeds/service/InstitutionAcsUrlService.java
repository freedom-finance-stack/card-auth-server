package com.razorpay.threeds.service;

import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;

public interface InstitutionAcsUrlService {
    public InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK)
            throws ACSDataAccessException, DataNotFoundException;
}
