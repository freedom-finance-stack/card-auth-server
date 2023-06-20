package com.razorpay.acs.dao.contract;

import lombok.Data;

@Data
public class CREQ extends ThreeDSObject {

  private String threeDSServerTransID;
}
