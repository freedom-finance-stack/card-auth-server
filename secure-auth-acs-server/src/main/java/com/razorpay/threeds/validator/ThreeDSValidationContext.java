/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2017  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Author 				: Ashish Kirpan
 * Created Date 			: Nov 29, 2017
 ******************************************************************************/

package com.razorpay.threeds.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.threeds.utils.Util;
import com.razorpay.threeds.validator.enums.ThreeDSDataElement;
import com.razorpay.threeds.validator.enums.ThreeDSDataElementValidationStrategy;

public class ThreeDSValidationContext {

  private Set<ValidationStrategy> areqValidationStrategies;

  public ThreeDSValidationContext(Set<ValidationStrategy> areqValidationStrategies) {
    this.areqValidationStrategies = areqValidationStrategies;
  }

  /*
   * This method performs validation for fields one by one and return the
   * invalid one if found. Otherwise, it will continue validating remaining
   * fields. If all the fields are valid then will return null.
   */
  public ThreeDSDataElement execute(AREQ objAReq) {
    ValidationStrategy areqValidation = ThreeDSDataElementValidationStrategy.UNSUPPORTED;
    for (Iterator<ValidationStrategy> iterator = areqValidationStrategies.iterator();
        iterator.hasNext(); ) {
      areqValidation = iterator.next();
      ValidationResponse validator = areqValidation.validateInclusion(objAReq);
      if (validator != null && validator.isInvalid()) {
        return areqValidation.getDataElement();
      }
    }
    return null;
  }

  /*
   * This method performs validation for fields one by one and add the invalid
   * one into a list if found and returns that list.
   */
  public List<ValidationResponse> executeAndGetList(AREQ objAReq) {
    ValidationStrategy areqValidation = ThreeDSDataElementValidationStrategy.UNSUPPORTED;
    List<ValidationResponse> validatedTypes = new ArrayList<ValidationResponse>();
    for (Iterator<ValidationStrategy> iterator = areqValidationStrategies.iterator();
        iterator.hasNext(); ) {
      areqValidation = iterator.next();

      ValidationResponse validator = areqValidation.validateInclusion(objAReq);
      if (Util.isNull(validator)) {
        validator = areqValidation.validateFormat(objAReq);
        if (Util.isNull(validator)) {
          validator = areqValidation.validateValue(objAReq);
          if (Util.isNotNull(validator)) {
            validatedTypes.add(validator);
          }
        } else if (validator.isInvalid()) {
          validatedTypes.add(validator);
        }
      } else if (validator.isInvalid()) {
        validatedTypes.add(validator);
      }
    }
    return validatedTypes;
  }
}
