package com.razorpay.threeds.validation.impl;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.threeds.validation.ValidationService;

import org.springframework.stereotype.Service;

@Service("aReqValidationServiceImpl")
public class AReqValidationServiceImpl implements ValidationService<AREQ> {

    @Override
    public void validate(AREQ areq) {
        // chain design pattern...  
        // if (reqAReq.getMessageType().equals(ThreeDSConstant.MESSAGE_TYPE_AREQ)
        // check for duplicate request
        //  check if card number exist and valid
    }
}
