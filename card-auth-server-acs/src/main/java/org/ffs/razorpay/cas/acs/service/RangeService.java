package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSException;
import org.ffs.razorpay.cas.dao.model.CardRange;

public interface RangeService {
    CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException;

    void validateRange(CardRange cardRange) throws ACSException, DataNotFoundException;
}
