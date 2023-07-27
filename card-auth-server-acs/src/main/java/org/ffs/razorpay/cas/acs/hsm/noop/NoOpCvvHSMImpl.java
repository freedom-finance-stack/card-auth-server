package org.ffs.razorpay.cas.acs.hsm.noop;

import org.ffs.razorpay.cas.acs.exception.HSMConnectionException;
import org.ffs.razorpay.cas.acs.hsm.CvvHSM;
import org.ffs.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

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
