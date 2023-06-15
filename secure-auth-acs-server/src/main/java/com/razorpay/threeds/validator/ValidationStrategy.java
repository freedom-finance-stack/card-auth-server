package com.razorpay.threeds.validator;

import com.razorpay.acs.dao.contract.ThreeDSObject;
import com.razorpay.threeds.validator.enums.ThreeDSDataElement;

public interface ValidationStrategy {

    <T extends ThreeDSObject> ValidationResponse validateInclusion(T input);

    <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(T input, R input2);

    <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(T input, R input2, R input3, R input4);

    <T extends ThreeDSObject> ValidationResponse validateFormat(T input);

    <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateFormat(T input, R input2, R input3, R input4);

    <T extends ThreeDSObject> ValidationResponse validateValue(T input);

    ThreeDSDataElement getDataElement();

}
