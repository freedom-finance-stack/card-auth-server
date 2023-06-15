package com.razorpay.threeds.service;

import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.threeds.exception.checked.DataNotFoundException;

public interface RangeService {
    CardRange findByPan(String  pan) throws DataNotFoundException;
}
