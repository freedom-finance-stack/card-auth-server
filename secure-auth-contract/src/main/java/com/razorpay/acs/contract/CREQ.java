package com.razorpay.acs.contract;

import lombok.Data;

@Data
public class CREQ extends ThreeDSObject {

  private String threeDSServerTransID;
}
