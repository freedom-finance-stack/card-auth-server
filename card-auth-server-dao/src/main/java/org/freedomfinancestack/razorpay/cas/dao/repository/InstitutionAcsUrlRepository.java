package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionAcsUrlPK;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionAcsUrlRepository
        extends BaseRepository<InstitutionAcsUrl, InstitutionAcsUrlPK> {}
