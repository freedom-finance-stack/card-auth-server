package com.razorpay.threeds.service.cardDetail;

import com.razorpay.acs.dao.enums.CardStoreType;

public interface CardDetailFetcherFactory {
  CardDetailFetcherService getCardDetailFetcher(CardStoreType cardDetailsFetcherType);
}
