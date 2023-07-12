package com.razorpay.threeds.context;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestContext implements Serializable {
    private String requestId;
}
