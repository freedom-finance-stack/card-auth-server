package com.razorpay.ffs.cas.acs.validator;

import com.razorpay.ffs.cas.acs.exception.ThreeDSException;
import com.razorpay.ffs.cas.contract.ThreeDSObject;

public interface ThreeDSValidator<T extends ThreeDSObject> {
    void validateRequest(T request) throws ThreeDSException;
}
