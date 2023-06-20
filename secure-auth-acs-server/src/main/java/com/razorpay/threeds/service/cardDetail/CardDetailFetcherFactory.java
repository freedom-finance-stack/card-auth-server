package com.razorpay.threeds.service.cardDetail;

import com.razorpay.acs.dao.enums.CardDetailsStore;

public interface CardDetailFetcherFactory {
  CardDetailFetcherService getCardDetailFetcher(CardDetailsStore cardDetailsFetcherType);
}
