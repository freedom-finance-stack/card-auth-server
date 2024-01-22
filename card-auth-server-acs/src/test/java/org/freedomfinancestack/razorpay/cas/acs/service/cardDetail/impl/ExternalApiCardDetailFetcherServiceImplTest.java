package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExternalApiCardDetailFetcherServiceImplTest {
    @Test
    public void getCardDetails() throws CardDetailsNotFoundException {
        ExternalApiCardDetailFetcherServiceImpl externalApiCardDetailFetcherService =
                new ExternalApiCardDetailFetcherServiceImpl();
        assertNull(
                externalApiCardDetailFetcherService.getCardDetails(mock(CardDetailsRequest.class)));
    }
}
