package org.ffs.razorpay.cas.acs.service.cardDetail;

import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.exception.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.checked.CardBlockedException;
import org.ffs.razorpay.cas.dao.enums.CardDetailsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardDetailService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final CardDetailFetcherFactory cardDetailFetcherFactory;

    public CardDetailResponse getCardDetails(
            CardDetailsRequest cardDetailsRequest, CardDetailsStore type)
            throws ACSDataAccessException {
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
