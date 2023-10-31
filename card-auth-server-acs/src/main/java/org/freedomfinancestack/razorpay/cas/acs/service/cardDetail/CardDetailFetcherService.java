package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;

/**
 * The {@code CardDetailFetcherService} interface defines the contract for fetching card details and
 * validating the card data.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface CardDetailFetcherService {

    /**
     * Retrieves card details based on the provided {@link CardDetailsRequest}.
     *
     * @param cardDetailsRequest the request object containing card details
     * @return the response object containing card details
     * @throws ACSDataAccessException if there is a data access issue while fetching card details
     */
    CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException;

    /**
     * Block the card based on the provided {@link CardDetailsRequest}
     *
     * @param cardDetailsRequest the request object containing card details to block the card
     * @throws ACSDataAccessException if there is a data access issue while blocking the card
     */
    void blockCard(CardDetailsRequest cardDetailsRequest) throws ACSDataAccessException;

    /**
     * Validates the provided card details in the {@link CardDetailResponse}.
     *
     * @param cardDetailResponse the response object containing card details
     * @throws CardBlockedException if the card is blocked or not allowed
     * @throws DataNotFoundException if the card details are not found
     */
    void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws CardBlockedException, DataNotFoundException, CardDetailsNotFoundException;
}
