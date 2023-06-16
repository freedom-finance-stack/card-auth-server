package com.razorpay.threeds.service;

import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.exception.DataNotFoundException;

public interface RangeService {
    CardRange findByPan(String  pan) throws DataNotFoundException;
    void validateRange(CardRange cardRange) throws ACSException;
}
