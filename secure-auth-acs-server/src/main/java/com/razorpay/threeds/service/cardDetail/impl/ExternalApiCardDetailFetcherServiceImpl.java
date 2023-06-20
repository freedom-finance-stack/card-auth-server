package com.razorpay.threeds.service.cardDetail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.CardDetailsStore;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.cardDetail.CardDetailFetcherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(CardDetailsStore.CardStoreTypeConstants.EXTERNAL_API)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExternalApiCardDetailFetcherServiceImpl implements CardDetailFetcherService {
  @Override
  public CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
      throws ACSException {
    log.info("Fetching card details from External API");
    return null;
  }

  public void validateCardDetails(CardDetailResponse cardDetailDto)
      throws ACSException, ThreeDSException {
    log.info("Validating card details from ACS");
  }
}
