package com.razorpay.threeds.service;


import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;

public interface InstitutionAcsUrlService {
    public InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK);
}