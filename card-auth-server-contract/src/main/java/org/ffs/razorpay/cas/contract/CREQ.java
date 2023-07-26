package org.ffs.razorpay.cas.contract;

import lombok.Data;

@Data
public class CREQ extends ThreeDSObject {

    private String threeDSServerTransID;
}
