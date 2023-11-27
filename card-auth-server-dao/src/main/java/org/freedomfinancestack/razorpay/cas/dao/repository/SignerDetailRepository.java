package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetail;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetailPK;
import org.springframework.stereotype.Repository;

@Repository
public interface SignerDetailRepository extends BaseRepository<SignerDetail, SignerDetailPK> {}
