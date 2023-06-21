package com.razorpay.threeds.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;
import com.razorpay.acs.dao.repository.InstitutionAcsUrlRepository;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.ThreeDSecureErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionAcsUrlServiceImpl
    implements com.razorpay.threeds.service.InstitutionAcsUrlService {
  private final InstitutionAcsUrlRepository institutionAcsUrlRepository;

  @Override
  public InstitutionAcsUrl findById(InstitutionAcsUrlPK institutionAcsUrlPK) {
    Optional<InstitutionAcsUrl> acsUrl = institutionAcsUrlRepository.findById(institutionAcsUrlPK);
    if (acsUrl.isPresent()) {
      return acsUrl.get();
    }
    throw new DataNotFoundException(
        ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
        "Acs url not found for Institution ID :"
            + institutionAcsUrlPK.getInstitutionId()
            + " and Network: "
            + institutionAcsUrlPK.getNetworkCode()
            + " and Channel: "
            + institutionAcsUrlPK.getDeviceChannel());
  }
}
