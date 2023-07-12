package com.razorpay.threeds.validator;

import com.razorpay.acs.contract.ThreeDSObject;
import com.razorpay.threeds.exception.ThreeDSException;

public interface ThreeDSValidator<T extends ThreeDSObject> {
  void validateRequest(T request) throws ThreeDSException;
}
