package com.razorpay.threeds.service.impl;

import com.razorpay.threeds.context.RequestContextHolder;
import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
import com.razorpay.threeds.configuration.AppConfiguration;
import com.razorpay.threeds.dto.TransactionDto;
import com.razorpay.threeds.service.AuthenticationService;
import com.razorpay.threeds.service.TransactionService;
import com.razorpay.threeds.validation.ValidationService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service("authenticationServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    @Qualifier(value = "aReqValidationServiceImpl")
    private final ValidationService<AREQ> validationService;
    private final TransactionService transactionService;
    private final AppConfiguration appConfiguration;
    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq) {

        log.info("Starting processing for Authentication request: {}", RequestContextHolder.get().getRequestId());
        String id = UUID.randomUUID().toString();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(id);
        transactionDto.setInstitutionId("random");
        transactionDto.setMessageCategory("AREQ");
        transactionDto.setDeviceChannel("BRW");
        transactionService.create(transactionDto);
        // Validate AREQ
        validationService.validate(areq);
        transactionService.removeAll();
        return null;
    }
}
