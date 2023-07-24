package com.razorpay.ffs.cas.acs.service.cardDetail;

import com.razorpay.ffs.cas.dao.enums.CardDetailsStore;

public interface CardDetailFetcherFactory {
    CardDetailFetcherService getCardDetailFetcher(CardDetailsStore cardDetailsFetcherType);
}
