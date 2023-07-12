package com.razorpay.threeds.hsm.noop;

import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.HSMConnectionException;
import com.razorpay.threeds.hsm.CvvHSM;

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
