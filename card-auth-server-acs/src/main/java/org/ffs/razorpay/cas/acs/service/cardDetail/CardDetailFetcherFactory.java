package org.ffs.razorpay.cas.acs.service.cardDetail;

import org.ffs.razorpay.cas.dao.enums.CardDetailsStore;

/**
 * The {@code CardDetailFetcherFactory} interface defines a factory for creating instances of {@link
 * CardDetailFetcherService} based on the specified {@link CardDetailsStore} type.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface CardDetailFetcherFactory {

    /**
     * Creates and returns an instance of {@link CardDetailFetcherService} based on the specified
     * {@link CardDetailsStore} type.
     *
     * @param cardDetailsFetcherType the type of card details store
     * @return an instance of {@link CardDetailFetcherService} for the specified store type
     */
    CardDetailFetcherService getCardDetailFetcher(CardDetailsStore cardDetailsFetcherType);
}
