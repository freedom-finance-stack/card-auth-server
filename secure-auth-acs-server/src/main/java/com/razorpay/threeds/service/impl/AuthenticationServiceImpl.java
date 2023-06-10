package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.TransactionStatus;
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

import java.sql.Timestamp;
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
        transactionDto.setPhase(Phase.AREQ);
        transactionDto.setTransactionStatus(TransactionStatus.CREATED);
        transactionService.create(transactionDto);
        // Validate AREQ
        TransactionDto data = transactionService.findById(id);
        TransactionDto data2 = transactionService.findById("e6413066-97a6-4389-88af-1e8d2485a29e");
        validationService.validate(areq);
        transactionService.remove(id);
        transactionService.removeAll();
        return null;
    }
}
