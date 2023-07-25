package com.razorpay.ffs.cas.acs.service;

import com.razorpay.ffs.cas.acs.dto.GenerateECIRequest;

public interface ECommIndicatorService {
    public String generateECI(GenerateECIRequest generateECIRequest);
}
