package com.razorpay.ffs.cas.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.ffs.cas.dao.model.HSMConfigPK;
import com.razorpay.ffs.cas.dao.model.HsmConfig;

@Repository
public interface HSMConfigRepository extends BaseRepository<HsmConfig, HSMConfigPK> {}
