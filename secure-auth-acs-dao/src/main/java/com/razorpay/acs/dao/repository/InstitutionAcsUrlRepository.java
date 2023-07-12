package com.razorpay.acs.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;

@Repository
public interface InstitutionAcsUrlRepository
        extends BaseRepository<InstitutionAcsUrl, InstitutionAcsUrlPK> {}
