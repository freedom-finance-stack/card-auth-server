package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExternalApiCardDetailFetcherServiceImplTest {
    ExternalApiCardDetailFetcherServiceImpl externalApiCardDetailFetcherService =
            new ExternalApiCardDetailFetcherServiceImpl();

    @Test
    public void getCardDetails() throws CardDetailsNotFoundException {
        assertNull(
                externalApiCardDetailFetcherService.getCardDetails(mock(CardDetailsRequest.class)));
    }

    @Test
    public void blockCard() throws CardDetailsNotFoundException {
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("institutionId", "12");
        externalApiCardDetailFetcherService.blockCard(cardDetailsRequest);
    }

    @Test
    public void validateCardDetails() throws CardDetailsNotFoundException, CardBlockedException {
        externalApiCardDetailFetcherService.validateCardDetails(new CardDetailResponse());
    }
}
