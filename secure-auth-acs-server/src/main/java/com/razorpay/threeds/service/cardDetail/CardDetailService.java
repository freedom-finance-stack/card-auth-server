package com.razorpay.threeds.service.cardDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.CardDetailsStore;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.CardBlockedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardDetailService {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private final CardDetailFetcherFactory cardDetailFetcherFactory;

  public CardDetailResponse getCardDetails(
      CardDetailsRequest cardDetailsRequest, CardDetailsStore type) throws ACSDataAccessException {
    CardDetailFetcherService cardDetailFetcherService =
        cardDetailFetcherFactory.getCardDetailFetcher(type);
    return cardDetailFetcherService.getCardDetails(cardDetailsRequest);
  }

  public void validateCardDetails(CardDetailResponse cardDetailResponse, CardDetailsStore type)
      throws DataNotFoundException, CardBlockedException {
    CardDetailFetcherService cardDetailFetcherService =
        cardDetailFetcherFactory.getCardDetailFetcher(type);
    cardDetailFetcherService.validateCardDetails(cardDetailResponse);
  }
}
