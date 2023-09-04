package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.RReqMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DSConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultRequestServiceImpl implements ResultRequestService {
    private final RReqMapper rReqMapper;

    @Override
    public void sendRreq(Transaction transaction)
            throws DSConnectionException, ValidationException {

        // createRReq
        RREQ rreq = rReqMapper.toRreq(transaction);

        // SendRReq with mock true and false by fetching networwise keystore and mock flag from
        // application.yml file
        // handle Error properly with retry mechanism

    }
}
