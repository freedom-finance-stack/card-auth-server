package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.ffs.razorpay.cas.dao.model.CardRange;

public interface RangeService {
    CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException;

    void validateRange(CardRange cardRange)
            throws TransactionDataNotValidException, DataNotFoundException;
}
