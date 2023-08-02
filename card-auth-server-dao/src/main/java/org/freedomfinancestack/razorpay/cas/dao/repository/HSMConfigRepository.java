package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.HSMConfigPK;
import org.freedomfinancestack.razorpay.cas.dao.model.HsmConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface HSMConfigRepository extends BaseRepository<HsmConfig, HSMConfigPK> {}
