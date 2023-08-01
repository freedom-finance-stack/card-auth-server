package org.ffs.razorpay.cas.acs.service.cardDetail;

import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.dao.enums.CardDetailsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * The {@code CardDetailService} class is a facade for fetching card details from different sources
 * using implementations of the {@link CardDetailFetcherService}. It provides methods to retrieve
 * card details and validate the card data from the specified {@link CardDetailsStore} type.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardDetailService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final CardDetailFetcherFactory cardDetailFetcherFactory;

    /**
     * Fetches card details based on the provided {@link CardDetailsRequest} and {@link
     * CardDetailsStore} type.
     *
     * @param cardDetailsRequest the card details request
     * @param type the type of card details store
     * @return the response containing the fetched card details
     * @throws ACSDataAccessException if there is an error while fetching card details
     */
    public CardDetailResponse getCardDetails(
            CardDetailsRequest cardDetailsRequest, CardDetailsStore type)
            throws ACSDataAccessException {
        CardDetailFetcherService cardDetailFetcherService =
                cardDetailFetcherFactory.getCardDetailFetcher(type);
        return cardDetailFetcherService.getCardDetails(cardDetailsRequest);
    }

    /**
     * Validates the card details from the provided {@link CardDetailResponse} based on the
     * specified {@link CardDetailsStore} type.
     *
     * @param cardDetailResponse the card detail response to validate
     * @param type the type of card details store
     * @throws DataNotFoundException if the card details are not found
     * @throws CardBlockedException if the card is blocked
     */
    public void validateCardDetails(CardDetailResponse cardDetailResponse, CardDetailsStore type)
            throws DataNotFoundException, CardBlockedException {
        CardDetailFetcherService cardDetailFetcherService =
                cardDetailFetcherFactory.getCardDetailFetcher(type);
        cardDetailFetcherService.validateCardDetails(cardDetailResponse);
    }
}
