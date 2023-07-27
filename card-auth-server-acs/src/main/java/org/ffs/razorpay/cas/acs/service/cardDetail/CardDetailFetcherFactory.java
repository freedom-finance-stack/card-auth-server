package org.ffs.razorpay.cas.acs.service.cardDetail;

import org.ffs.razorpay.cas.dao.enums.CardDetailsStore;

public interface CardDetailFetcherFactory {
    CardDetailFetcherService getCardDetailFetcher(CardDetailsStore cardDetailsFetcherType);
}
