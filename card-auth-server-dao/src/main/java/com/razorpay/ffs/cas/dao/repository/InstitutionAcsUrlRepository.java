package com.razorpay.ffs.cas.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.ffs.cas.dao.model.InstitutionAcsUrl;
import com.razorpay.ffs.cas.dao.model.InstitutionAcsUrlPK;

@Repository
public interface InstitutionAcsUrlRepository
        extends BaseRepository<InstitutionAcsUrl, InstitutionAcsUrlPK> {}
