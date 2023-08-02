package org.ffs.razorpay.cas.acs.validator;

import org.ffs.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.ffs.razorpay.cas.contract.ThreeDSObject;

public interface ThreeDSValidator<T extends ThreeDSObject> {
    void validateRequest(T request) throws ThreeDSException;
}
