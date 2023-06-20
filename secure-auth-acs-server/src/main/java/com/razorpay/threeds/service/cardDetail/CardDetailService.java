package com.razorpay.threeds.service.cardDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.CardStoreType;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.exception.checked.ACSException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardDetailService {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private final CardDetailFetcherFactory cardDetailFetcherFactory;

  public CardDetailResponse getCardDetails(
      CardDetailsRequest cardDetailsRequest, CardStoreType type) throws ACSException {
    CardDetailFetcherService cardDetailFetcherService =
        cardDetailFetcherFactory.getCardDetailFetcher(type);
    return cardDetailFetcherService.getCardDetails(cardDetailsRequest);
  }

  public void validateCardDetails(CardDetailResponse cardDetailResponse, CardStoreType type)
      throws ACSException {
    CardDetailFetcherService cardDetailFetcherService =
        cardDetailFetcherFactory.getCardDetailFetcher(type);
    cardDetailFetcherService.validateCardDetails(cardDetailResponse);
  }
}
