package com.razorpay.threeds.service;

import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.ACSException;

public interface RangeService {
    CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException;

    void validateRange(CardRange cardRange) throws ACSException, DataNotFoundException;
}
