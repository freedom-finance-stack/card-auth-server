package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DSConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class ResultRequestServiceImpl implements ResultRequestService {
    @Override
    public void sendRreq(Transaction transaction)
            throws DSConnectionException, ValidationException {}
}
