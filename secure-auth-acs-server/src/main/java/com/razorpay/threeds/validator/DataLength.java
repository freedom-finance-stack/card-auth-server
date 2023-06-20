/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2017  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Author 				: Ashish Kirpan
 * Created Date 			: Dec 20, 2017
 ******************************************************************************/

package com.razorpay.threeds.validator;

import com.razorpay.threeds.validator.enums.DataLengthType;

public class DataLength {

  private int length;
  private DataLengthType lengthType;

  public DataLength() {}

  public DataLength(DataLengthType lengthType) {
    this.lengthType = lengthType;
  }

  public DataLength(int length, DataLengthType lengthType) {
    this.length = length;
    this.lengthType = lengthType;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public DataLengthType getLengthType() {
    return lengthType;
  }

  public void setLengthType(DataLengthType lengthType) {
    this.lengthType = lengthType;
  }
}
