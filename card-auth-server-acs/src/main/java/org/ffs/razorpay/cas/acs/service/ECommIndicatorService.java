package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.dto.GenerateECIRequest;

public interface ECommIndicatorService {
    String generateECI(GenerateECIRequest generateECIRequest);
}
