package com.razorpay.ffs.cas.acs.hsm.noop;

import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.exception.HSMConnectionException;
import com.razorpay.ffs.cas.acs.hsm.CvvHSM;
import com.razorpay.ffs.cas.dao.model.Transaction;

import lombok.NonNull;

@Service("noOpCvvHSMImpl")
public class NoOpCvvHSMImpl implements CvvHSM {

    public static final String NO_OP_CVV_OUTPUT = "123";

    @Override
    public String generateCVV(@NonNull final Transaction transaction, @NonNull final String data)
            throws HSMConnectionException {
        return NO_OP_CVV_OUTPUT;
    }
}
