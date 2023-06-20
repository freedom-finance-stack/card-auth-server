package com.razorpay.threeds.service.cardDetail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.CardStoreType;
import com.razorpay.acs.dao.repository.CardRangeRepository;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.exception.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.UserBlockedException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.cardDetail.CardDetailFetcherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(CardStoreType.CardStoreTypeConstants.ACS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSCardDetailFetcherServiceImpl implements CardDetailFetcherService {
  private final CardRangeRepository cardRangeRepository;

  public CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
      throws ACSException {
    log.info("Fetching card details from ACS");
    return null;
  }

  public void validateCardDetails(CardDetailResponse cardDetailResponse) throws ThreeDSException {
    if (!cardDetailResponse.isSuccess() || cardDetailResponse.getCardDetailDto() == null) {
      throw new DataNotFoundException(
          ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, "Card Number not found");
    } else if (cardDetailResponse.getCardDetailDto().isBlocked()
        || !cardDetailResponse.getCardDetailDto().isEnrolled()) {
      throw new UserBlockedException(
          ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, "Card Number is blocked");
    }
  }
}
