package com.razorpay.threeds.validator.enums;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ThreeDSObject;
import com.razorpay.acs.dao.contract.ThreeDSRequestorAuthenticationInfo;
import com.razorpay.acs.dao.contract.enums.DeviceChannel;
import com.razorpay.acs.dao.contract.enums.MessageCategory;
import com.razorpay.threeds.exception.ThreeDSecureErrorCode;
import com.razorpay.threeds.utils.Util;
import com.razorpay.threeds.validator.ValidationResponse;
import com.razorpay.threeds.validator.ValidationStrategy;

public enum ThreeDSDataElementValidationStrategy implements ValidationStrategy {

  // -------------------------------------------------------------------------------------------------------------
  THREEDS_COMPIND(ThreeDSDataElement.THREEDS_COMPIND) {

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getThreeDSCompInd());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateFieldLength(objAReq.getThreeDSCompInd());
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateFieldValue(objAReq.getThreeDSCompInd());
      }

      return null;
    }
  },

  THREEDS_REQUESTOR_AUTHENTICATION_IND(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getThreeDSRequestorAuthenticationInd());
      }
      return null;
    }

    /*@Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
    	AREQ objAReq = (AREQ) input;
    	return validateFieldLength(objAReq.getThreeDSRequestorAuthenticationInd());
    }*/

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateFieldValue(objAReq.getThreeDSRequestorAuthenticationInd());
      }
      return null;
    }
  },

  THREEDS_REQUESTOR_AUTHENTICATION_INFO(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_INFO) {

    /*@Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
    	AREQ objAReq = (AREQ) input;

    	if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
    			&& MessageCategory.contains(getDataElement().supportedCategory, objAReq.getMessageCategory())
    			&& DeviceChannel.contains(getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
    		return validateEmptyField(objAReq.getThreeDSRequestorAuthenticationInd());
    	}
    	return null;
    }*/

    /*@Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
    	AREQ objAReq = (AREQ) input;
    	String value = objAReq.getThreeDSRequestorAuthenticationInfo();
    	if (Util.isNotNull(value)) {
    		return validateFieldLength(value);
    	}
    	return null;
    }*/

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
      AREQ objAReq = (AREQ) input;
      ThreeDSRequestorAuthenticationInfo value = objAReq.getThreeDSRequestorAuthenticationInfo();
      if (Util.isNotNull(value)) {
        if (!value.isValid()) {
          ValidationResponse v =
              new ValidationResponse(
                  true, ThreeDSecureErrorCode.INVALID_FORMAT, getDataElement(), value.toString());
          return v;
        }
      }
      return null;
    }
  },

  MESSAGE_CATEGORY(ThreeDSDataElement.MESSAGE_CATEGORY) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getMessageCategory());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getMessageCategory();
      if (Util.isNotNull(value)) {
        return validateFieldLength(value);
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getMessageCategory();
      ValidationResponse respnose = null;

      if (Util.isNotNull(value)) {
        respnose = validateFieldValue(value);
      }

      if (
      /*Utility.isNull(respnose)*/ "2.1.0".equals(objAReq.getMessageVersion())
          && MessageCategory.PA.getCategory().equals(value)
          && DeviceChannel.TRI.getChannel().equals(objAReq.getDeviceChannel())) {
        respnose =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, getDataElement(), value);
      }

      return respnose;
    }
  },
  MESSAGE_TYPE(ThreeDSDataElement.MESSAGE_TYPE) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {

      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getMessageType());
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {

      if (input instanceof AREQ) {
        AREQ objAReq = (AREQ) input;
        String value = objAReq.getMessageType();
        if (Util.isNotNull(value)) {
          return validateFieldLength(value);
        }
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {

      if (input instanceof AREQ) {
        AREQ objAReq = (AREQ) input;
        String value = objAReq.getMessageType();
        if (Util.isNotNull(value)) {
          ValidationResponse v = validateFieldValue(value);
          if (v != null) {
            v =
                new ValidationResponse(
                    true, ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID, getDataElement(), value);
          } else if (!value.equals("AReq")) {
            v =
                new ValidationResponse(
                    true, ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID, getDataElement(), value);
          }
          return v;
        }
      }

      return null;
    }
  },

  MESSAGE_VERSION(ThreeDSDataElement.MESSAGE_VERSION) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {

      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getMessageVersion());
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {

      if (input instanceof AREQ) {
        AREQ objAReq = (AREQ) input;
        String value = objAReq.getMessageVersion();
        if (Util.isNotNull(value)) {
          return validateFieldLength(value);
        }
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {

      if (input instanceof AREQ) {
        AREQ objAReq = (AREQ) input;
        String value = objAReq.getMessageVersion();
        if (Util.isNotNull(value)) {
          ValidationResponse v = validateFieldValue(value);
          if (v != null) {
            v =
                new ValidationResponse(
                    true,
                    ThreeDSecureErrorCode.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED,
                    getDataElement(),
                    v.getValue());
          }
          return v;
        }
      }

      return null;
    }
  },

  NOTIFICATION_URL(ThreeDSDataElement.NOTIFICATION_URL) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getNotificationURL());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getNotificationURL();
      if (Util.isNotNull(value)) {
        return validateFieldLength(value);
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {

      AREQ objAReq = (AREQ) input;
      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        String value = objAReq.getNotificationURL();
        if (Util.isNull(value)) {
          ValidationResponse v =
              new ValidationResponse(
                  true,
                  ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                  getDataElement(),
                  value);
          return v;
        }
      }

      return null;
    }
  },

  THREEDS_REQUESTOR_CHALLENGE_IND(ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND) {

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getThreeDSRequestorChallengeInd();
      if (Util.isNotNull(value)) {
        return validateFieldLength(value);
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getThreeDSRequestorChallengeInd();
      if (Util.isNotNull(value)) {
        return validateFieldValue(value);
      }

      return null;
    }
  },

  THREEDS_REQUESTOR_ID(ThreeDSDataElement.THREEDS_REQUESTOR_ID) {

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getThreeDSRequestorID());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      if (Util.isNotNull(objAReq.getThreeDSRequestorID())) {
        return validateFieldLength(objAReq.getThreeDSRequestorID());
      }
      return null;
    }
  },

  THREEDS_REQUESTOR_NAME(ThreeDSDataElement.THREEDS_REQUESTOR_NAME) {

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getThreeDSRequestorName());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getThreeDSRequestorName();
      if (Util.isNotNull(value)) {
        return validateFieldLength(value);
      }
      return null;
    }
  },

  THREEDS_REQUESTOR_URL(ThreeDSDataElement.THREEDS_REQUESTOR_URL) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getThreeDSRequestorURL());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getThreeDSRequestorURL();
      if (Util.isNotNull(value)) {
        return validateFieldLength(value);
      }

      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateValue(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getThreeDSRequestorURL();
      if (Util.isNull(value)) {
        ValidationResponse v =
            new ValidationResponse(
                true, ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, getDataElement(), value);
        return v;
      }
      return null;
    }
  },

  THREEDS_SERVER_REF_NUMBER(ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER) {
    @Override
    public <T extends ThreeDSObject> ValidationResponse validateInclusion(T input) {
      AREQ objAReq = (AREQ) input;

      if (getDataElement().inclusion.equals(MessageInclusion.REQUIRED)
          && MessageCategory.contains(
              getDataElement().supportedCategory, objAReq.getMessageCategory())
          && DeviceChannel.contains(
              getDataElement().supportedChannel, objAReq.getDeviceChannel())) {
        return validateEmptyField(objAReq.getThreeDSServerRefNumber());
      }
      return null;
    }

    @Override
    public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
      AREQ objAReq = (AREQ) input;
      String value = objAReq.getThreeDSServerRefNumber();
      if (Util.isNotNull(value)) {
        return validateFieldLength(value);
      }

      return null;
    }
  },

  UNSUPPORTED(ThreeDSDataElement.UNSUPPORTED) {};

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
  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(
      T input, R input2) {
    return null;
  }

  @Override
  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateInclusion(
      T input, R input2, R input3, R input4) {
    return this.validateInclusion(input, input2);
  }

  @Override
  public <T extends ThreeDSObject> ValidationResponse validateFormat(T input) {
    return null;
  }

  @Override
  public <T extends ThreeDSObject, R extends ThreeDSObject> ValidationResponse validateFormat(
      T input, R input2, R input3, R input4) {
    return this.validateFormat(input);
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
