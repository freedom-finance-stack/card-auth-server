package com.razorpay.ffs.cas.acs.service.cardDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.dto.CardDetailResponse;
import com.razorpay.ffs.cas.acs.dto.CardDetailsRequest;
import com.razorpay.ffs.cas.acs.exception.DataNotFoundException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.exception.checked.CardBlockedException;
import com.razorpay.ffs.cas.dao.enums.CardDetailsStore;

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
