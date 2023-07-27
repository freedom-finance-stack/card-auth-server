package org.ffs.razorpay.cas.dao.repository;

import org.ffs.razorpay.cas.dao.model.HSMConfigPK;
import org.ffs.razorpay.cas.dao.model.HsmConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface HSMConfigRepository extends BaseRepository<HsmConfig, HSMConfigPK> {}
