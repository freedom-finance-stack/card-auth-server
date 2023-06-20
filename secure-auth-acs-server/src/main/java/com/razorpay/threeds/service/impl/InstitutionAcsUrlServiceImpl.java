package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;
import com.razorpay.acs.dao.repository.InstitutionAcsUrlRepository;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.ThreeDSecureErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionAcsUrlServiceImpl implements com.razorpay.threeds.service.InstitutionAcsUrlService{
    private final InstitutionAcsUrlRepository institutionAcsUrlRepository;

    @Override
    public InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK) {
        if (institutionAcsUrlRepository.findById(institutionAcsUrlPK).isPresent()){
            return institutionAcsUrlRepository.findById(institutionAcsUrlPK).get();
        }
        throw new DataNotFoundException(ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, "Institution Instrument Object not found for Institution ID :"
                + institutionAcsUrlPK.getInstitutionId() + " and Network: "
                + institutionAcsUrlPK.getNetworkCode() + " and Channel: "
                + institutionAcsUrlPK.getDeviceChannel());
    }
}