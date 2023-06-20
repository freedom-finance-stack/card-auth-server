package com.razorpay.threeds.validator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.validator.enums.ThreeDSDataElementValidationStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "authenticationRequestValidator")
public class AuthenticationRequestValidator implements ThreeDSValidator<AREQ> {

  @Override
  public void validateRequest(AREQ request) throws ValidationException {
    validateAuthenticationRequest(request);
  }

  protected void validateAuthenticationRequest(AREQ request) throws ValidationException {
    validateMandatoryFields(request);
    //        validateConditionalFields(request);
    //        validateOptionalFields(request);
    // more validation methods if required
  }

  protected void validateMandatoryFields(AREQ request) throws ValidationException {
    Set<ValidationStrategy> strategies = new LinkedHashSet<>();

    strategies.add(ThreeDSDataElementValidationStrategy.MESSAGE_CATEGORY);
    strategies.add(ThreeDSDataElementValidationStrategy.MESSAGE_TYPE);
    strategies.add(ThreeDSDataElementValidationStrategy.MESSAGE_VERSION);
    strategies.add(ThreeDSDataElementValidationStrategy.NOTIFICATION_URL);

    List<ValidationResponse> arrResultActual = validateFields(strategies, request);
    if (arrResultActual.size() > 0) {
      ValidationResponse v = arrResultActual.get(0);
      throw new ValidationException(v.getThreeDSecureError(), v.getDataElement().getFieldName());
    }
  }

  //    protected void validateConditionalFields(AREQ request) throws ValidationException {
  //        AREQ objAReq = (AREQ) request;
  //        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
  //
  //        //Conditional Fields
  //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_SERVER_OPERATOR_ID);
  //        strategies.add(ThreeDSDataElementValidationStrategy.ACCT_TYPE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BROAD_INFO);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BROWSER_IP);
  //        strategies.add(ThreeDSDataElementValidationStrategy.CARD_EXPIRY_DATE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_CITY);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_COUNTRY);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_LINE_1);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_LINE_2);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_LINE_3);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_POST_CODE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_STATE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.EMAIL);
  //        strategies.add(ThreeDSDataElementValidationStrategy.HOME_PHONE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.MOBILE_PHONE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.CARDHOLDER_NAME);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_CITY);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_COUNTRY);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_LINE_1);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_LINE_2);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_LINE_3);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_POST_CODE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_STATE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.WORK_PHONE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.DEVICE_INFO);
  //        strategies.add(ThreeDSDataElementValidationStrategy.DS_REFERENCE_NUMBER);
  //        strategies.add(ThreeDSDataElementValidationStrategy.DS_TRANS_ID);
  //        strategies.add(ThreeDSDataElementValidationStrategy.DS_URL);
  //        strategies.add(ThreeDSDataElementValidationStrategy.PAY_TOKEN_IND);
  //        strategies.add(ThreeDSDataElementValidationStrategy.PURCHASE_INSTAL_DATA);
  //        strategies.add(ThreeDSDataElementValidationStrategy.MESSAGE_EXTENSION);
  //        strategies.add(ThreeDSDataElementValidationStrategy.RECURRING_EXPIRY);
  //        strategies.add(ThreeDSDataElementValidationStrategy.RECURRING_FREQUENCY);
  //        strategies.add(ThreeDSDataElementValidationStrategy.SDK_ENC_DATA);
  //        strategies.add(ThreeDSDataElementValidationStrategy.TRANS_TYPE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.ACCT_INFO);
  //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQ_AUTH_METHOD_IND);
  //        strategies.add(ThreeDSDataElementValidationStrategy.PAY_TOKEN_SOURCE);
  //        strategies.add(ThreeDSDataElementValidationStrategy.WHITE_LIST_STATUS_SOURCE);
  //
  //        List<ValidationResponse> arrResultActual = validateFields(strategies, objAReq);
  //        if(arrResultActual.size() > 0){
  //
  //            //Updated for Visa PIT
  //            String errorDetail = null;
  //            ThreeDSecureError error = null;
  //            for(ValidationResponse v : arrResultActual) {
  //                if(errorDetail != null) {
  //                    errorDetail += Constant.COMMA;
  //                    errorDetail += v.getDataElement().getFieldName();
  //                } else {
  //                    errorDetail = v.getDataElement().getFieldName();
  //                }
  //                error = v.getThreeDSecureError();
  //            }
  //            throw new ValidationException(error, errorDetail);
  //
  //            //ValidationResponse v = arrResultActual.get(0);
  //            //throw new ValidationException(v.getThreeDSecureError(),
  // v.getDataElement().getFieldName());
  //        }
  //    }
  //
  //    protected void validateOptionalFields(AREQ request) throws ValidationException {
  //        AREQ objAReq = (AREQ) request;
  //        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
  //
  //        //Optional Fields
  //
  // strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_AUTHENTICATION_INFO);
  //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_CHALLENGE_IND);
  //
  // strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO);
  //        strategies.add(ThreeDSDataElementValidationStrategy.ADDRESS_MATCH);
  //        strategies.add(ThreeDSDataElementValidationStrategy.ACCT_ID);
  //        strategies.add(ThreeDSDataElementValidationStrategy.MERCHANT_RISK_INDICATOR);
  //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_DEC_MAX_TIME);
  //        strategies.add(ThreeDSDataElementValidationStrategy.WHITE_LIST_STATUS);
  //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_DEC_REQ_IND);
  //
  //        List<ValidationResponse> arrResultActual = validateFields(strategies, objAReq);
  //        if(arrResultActual.size() > 0){
  //            ValidationResponse v = arrResultActual.get(0);
  //            throw new ValidationException(v.getThreeDSecureError(),
  // v.getDataElement().getFieldName());
  //        }
  //
  //    }
  //

  private List<ValidationResponse> validateFields(
      Set<ValidationStrategy> strategies, AREQ objAReq) {

    ThreeDSValidationContext areqValidationContext = new ThreeDSValidationContext(strategies);
    List<ValidationResponse> arrResultActual = areqValidationContext.executeAndGetList(objAReq);
    log.trace("AREQ : Number of Invalid Fields : " + arrResultActual.size());
    return arrResultActual;
    //        for (ValidationResponse v : arrResultActual) {
    //            log.trace(v.toString());
    //
    // if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //
    // if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //            if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //            if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //            if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.INVALID_FORMAT)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //
    // if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //
    // if(v.getThreeDSecureError().equals(ThreeDSecureErrorCode.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED)) {
    //                arrResultActual.set(0, v);
    //                errorList.add(v);
    //            }
    //
    //        }
    //        return errorList;
  }
}
