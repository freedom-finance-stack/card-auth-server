package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardDetailsStore;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionCardHolderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * The {@code CardDetailService} class is a facade for fetching card details from different sources
 * using implementations of the {@link CardDetailFetcherService}. It provides methods to retrieve
 * card details and validate the card data from the specified {@link CardDetailsStore} type.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
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
     * block card provided in {@link CardDetailsRequest} and {@link CardDetailsStore} type.
     *
     * @param cardDetailsRequest the card details request
     * @param type the type of card details store
     * @throws ACSDataAccessException if there is an error while fetching card details
     */
    public void blockCard(CardDetailsRequest cardDetailsRequest, CardDetailsStore type)
            throws ACSDataAccessException {
        CardDetailFetcherService cardDetailFetcherService =
                cardDetailFetcherFactory.getCardDetailFetcher(type);
        cardDetailFetcherService.blockCard(cardDetailsRequest);
    }

    /**
     * Validates the card details by fetching the card details from store using {@link
     * CardDetailsRequest} based on the specified {@link CardDetailsStore} type.
     *
     * @param transaction to store the card detail response to transaction
     * @param cardDetailsRequest the card detail response to validate
     * @param cardDetailsStoreType the type of card details store
     * @throws DataNotFoundException if the card details are not found
     * @throws CardBlockedException if the card is blocked
     * @throws ACSDataAccessException if there is an error while fetching card details
     */
    public void validateAndUpdateCardDetails(
            Transaction transaction,
            CardDetailsRequest cardDetailsRequest,
            CardDetailsStore cardDetailsStoreType)
            throws DataNotFoundException,
                    CardBlockedException,
                    ACSDataAccessException,
                    CardDetailsNotFoundException {
        CardDetailResponse cardDetailResponse =
                getCardDetails(cardDetailsRequest, cardDetailsStoreType);
        CardDetailFetcherService cardDetailFetcherService =
                cardDetailFetcherFactory.getCardDetailFetcher(cardDetailsStoreType);
        cardDetailFetcherService.validateCardDetails(cardDetailResponse);
        TransactionCardHolderDetail transactionCardHolderDetail =
                TransactionCardHolderDetail.builder()
                        .mobileNumber(cardDetailResponse.getCardDetailDto().getMobileNumber())
                        .emailId(cardDetailResponse.getCardDetailDto().getEmailId())
                        .name(cardDetailResponse.getCardDetailDto().getName())
                        .build();
        transaction.setTransactionCardHolderDetail(transactionCardHolderDetail);
    }
}
