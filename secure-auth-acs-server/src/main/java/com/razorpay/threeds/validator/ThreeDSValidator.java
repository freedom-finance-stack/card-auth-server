package com.razorpay.threeds.validator;

import com.razorpay.acs.dao.contract.ThreeDSObject;
import com.razorpay.threeds.exception.ThreeDSException;

public interface ThreeDSValidator<T extends ThreeDSObject> {
  public abstract void validateRequest(T request) throws ThreeDSException;
}
