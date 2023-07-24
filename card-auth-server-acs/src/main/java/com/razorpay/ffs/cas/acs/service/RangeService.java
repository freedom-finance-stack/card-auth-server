package com.razorpay.ffs.cas.acs.service;

import com.razorpay.ffs.cas.acs.exception.DataNotFoundException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSException;
import com.razorpay.ffs.cas.dao.model.CardRange;

public interface RangeService {
    CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException;

    void validateRange(CardRange cardRange) throws ACSException, DataNotFoundException;
}
