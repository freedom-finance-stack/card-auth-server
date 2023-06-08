package com.razorpay.threeds.service.impl;

import com.razorpay.threeds.context.RequestContextHolder;
import com.razorpay.threeds.contract.AREQ;
import com.razorpay.threeds.contract.ARES;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.service.AuthenticationService;
import com.razorpay.threeds.validation.ValidationService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("authenticationServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    @Qualifier(value = "aReqValidationServiceImpl")
    private final ValidationService<AREQ> validationService;

    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq) {

        log.info("Starting processing for Authentication request: {}", RequestContextHolder.get().getRequestId());

        // Validate AREQ
        validationService.validate(areq);
        return null;
    }
}
