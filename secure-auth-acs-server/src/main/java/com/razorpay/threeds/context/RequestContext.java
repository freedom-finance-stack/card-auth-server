package com.razorpay.threeds.context;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RequestContext implements Serializable {

    private String requestId;
}
