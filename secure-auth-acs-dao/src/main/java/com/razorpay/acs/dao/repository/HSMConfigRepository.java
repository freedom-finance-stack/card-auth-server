package com.razorpay.acs.dao.repository;

import org.springframework.stereotype.Repository;

import com.razorpay.acs.dao.model.HSMConfigPK;
import com.razorpay.acs.dao.model.HsmConfig;

@Repository
public interface HSMConfigRepository extends BaseRepository<HsmConfig, HSMConfigPK> {}
