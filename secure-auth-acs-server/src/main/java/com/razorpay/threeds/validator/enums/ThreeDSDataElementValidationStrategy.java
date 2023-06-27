package com.razorpay.threeds.validator.enums;

import com.razorpay.acs.contract.ThreeDSObject;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.utils.Util;
import com.razorpay.threeds.validator.ValidationResponse;
import com.razorpay.threeds.validator.ValidationStrategy;

public enum ThreeDSDataElementValidationStrategy implements ValidationStrategy {
  ;

  // -------------------------------------------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  private ThreeDSDataElement dataElement;

  private ThreeDSDataElementValidationStrategy(ThreeDSDataElement validationType) {
     this.dataElement = validationType;
  }

  @Override
  public ThreeDSDataElement getDataElement() {
    return dataElement;
  }

  @Override
  public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
    return null;
  }

  @Override
  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(T input, R input2) {
    return null;
  }

  @Override
  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(T input, R input2, R input3, R input4) {
    return null;
  }

//  @Override
//  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(
//      T input, R input2) {
//    return null;
//  }
//
//  @Override
//  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(
//      T input, R input2, R input3, R input4) {
//    return this.validateInclusion(input, input2);
//  }

  @Override
  public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
    return null;
  }

  @Override
  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateFormat(T input, R input2, R input3, R input4) {
    return null;
  }

  @Override
  public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
    return null;
  }

  // ------------------------------------------------------------------------------

  public ValidationResponse validateEmptyField(String object) {

    if (Util.isNullorBlank(object)) {
      ValidationResponse v =
          new ValidationResponse(
              true, ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, getDataElement(), object);
      return v;
    }

    if (Util.containsAllSpaces(object)) {
      ValidationResponse v =
          new ValidationResponse(
              true, ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, getDataElement(), object);
      return v;
    }

    return null;
  }

  public ValidationResponse validateEmptyField(Object object) {

    if (Util.isNullorBlank(object)) {
      ValidationResponse v =
          new ValidationResponse(
              true, ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, getDataElement());
      return v;
    }

    return null;
  }

  public ValidationResponse validateFieldLength(String object) {

    if (DataLengthType.FIXED.equals(getDataElement().getLength().getLengthType())) {

      if (null != object && getDataElement().getLength().getLength() != object.length()) {
        ValidationResponse v =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, getDataElement(), object);
        return v;
      }

    } else if (DataLengthType.VARIABLE.equals(getDataElement().getLength().getLengthType())) {

      if (object.length() == 0) {
        ValidationResponse v =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, getDataElement(), object);
        return v;
      }

      if (object.length() > getDataElement().getLength().getLength()) {
        ValidationResponse v =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, getDataElement(), object);
        return v;
      }

    } else if (DataLengthType.BOOLEAN.equals(getDataElement().getLength().getLengthType())) {

      if (null != object && (object.length() < 4 || object.length() > 5)) {
        ValidationResponse v =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, getDataElement(), object);
        return v;
      }

    } else if (DataLengthType.JSON.equals(getDataElement().getLength().getLengthType())) {

      if (object.length() > getDataElement().getLength().getLength()) {
        ValidationResponse v =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, getDataElement(), object);
        return v;
      }
    }

    return null;
  }

  public ValidationResponse validateFieldValue(String object) {

    boolean hasError = false;
    for (String value : getDataElement().acceptedValues) {
      if (value.equals(object)) {
        hasError = true;
      }
    }

    if (!hasError) {
      ValidationResponse v =
          new ValidationResponse(
              true, ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, getDataElement(), object);
      return v;
    }
    return null;
  }

  public ValidationResponse validateFieldValueFormat(String object) {

    boolean hasError = false;

    if (!Util.isValidDate(object, getDataElement().acceptedFormat)) {
      hasError = true;
    }

    if (hasError) {
      ValidationResponse v =
          new ValidationResponse(
              true, ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, getDataElement(), object);
      return v;
    }
    return null;
  }
}
