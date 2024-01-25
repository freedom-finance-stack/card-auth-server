package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail;

import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardDetailsStore;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardDetailServiceTest {
    @Mock CardDetailFetcherFactory cardDetailFetcherFactory;
    @InjectMocks CardDetailService cardDetailService;

    @Test
    public void getCardDetails_ExceptionCase() throws CardDetailsNotFoundException {
        // Arrange

        CardDetailsRequest cardDetailsRequest =
                new CardDetailsRequest("institutionId", "cardNumber");
        CardDetailsStore type = CardDetailsStore.ACS;

        CardDetailFetcherService cardDetailFetcherService = mock(CardDetailFetcherService.class);

        when(cardDetailFetcherFactory.getCardDetailFetcher(type))
                .thenReturn(cardDetailFetcherService);

        doThrow(CardDetailsNotFoundException.class)
                .when(cardDetailFetcherService)
                .getCardDetails(cardDetailsRequest);

        // Act and Assert
        assertThrows(
                CardDetailsNotFoundException.class,
                () -> {
                    cardDetailService.getCardDetails(cardDetailsRequest, type);
                });
    }

    @Test
    public void getCardDetails() throws CardDetailsNotFoundException {

        CardDetailFetcherService mockFetcherService = Mockito.mock(CardDetailFetcherService.class);

        CardDetailResponse mockResponse = new CardDetailResponse();
        mockResponse.setSuccess(true);

        Mockito.when(mockFetcherService.getCardDetails(Mockito.any(CardDetailsRequest.class)))
                .thenReturn(mockResponse);

        CardDetailFetcherFactory mockFetcherFactory = Mockito.mock(CardDetailFetcherFactory.class);

        Mockito.when(mockFetcherFactory.getCardDetailFetcher(Mockito.any(CardDetailsStore.class)))
                .thenReturn(mockFetcherService);

        CardDetailService cardDetailService = new CardDetailService(mockFetcherFactory);

        CardDetailsRequest mockRequest = new CardDetailsRequest("institutionId", "cardNumber");

        CardDetailResponse result =
                cardDetailService.getCardDetails(mockRequest, CardDetailsStore.ACS);
        assertTrue(result.isSuccess());
    }

    @Test
    public void blockCard() throws CardDetailsNotFoundException {
        CardDetailsRequest mockRequest = new CardDetailsRequest("institutionId", "cardNumber");
        CardDetailFetcherService cardDetailFetcherService = mock(CardDetailFetcherService.class);

        when(cardDetailFetcherFactory.getCardDetailFetcher(any()))
                .thenReturn(cardDetailFetcherService);
        cardDetailService.blockCard(mockRequest, CardDetailsStore.ACS);
        verify(cardDetailFetcherService).blockCard(any());
    }

    @Test
    public void validateAndUpdateCardDetails()
            throws CardDetailsNotFoundException,
                    CardBlockedException,
                    DataNotFoundException,
                    ACSDataAccessException {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        // taken authentic data from Dev-dml database script
        CardDetailsRequest cardDetailRequest = new CardDetailsRequest("I1", "4016000000000018");
        CardDetailsStore cardDetailsStore = CardDetailsStore.ACS;
        CardDetailFetcherService cardDetailFetcherService = mock(CardDetailFetcherService.class);
        CardDetailService cardDetailService = new CardDetailService(cardDetailFetcherFactory);

        CardDetailResponse cardDetailResponse = new CardDetailResponse();
        cardDetailResponse.setSuccess(true);
        cardDetailResponse.setStatusCode("200");
        cardDetailResponse.setStatusDescription("Success");
        CardDetailDto cardDetailDto =
                CardDetailDto.builder()
                        .mobileNumber("9411234567")
                        .emailId("v@gmail.com")
                        .name("Varun")
                        .build();
        cardDetailResponse.setCardDetailDto(cardDetailDto);

        when(cardDetailFetcherFactory.getCardDetailFetcher(cardDetailsStore))
                .thenReturn(cardDetailFetcherService);
        when(cardDetailFetcherService.getCardDetails(cardDetailRequest))
                .thenReturn(cardDetailResponse);

        // ACT
        cardDetailService.validateAndUpdateCardDetails(
                transaction, cardDetailRequest, cardDetailsStore);

        verify(cardDetailFetcherFactory, atLeastOnce()).getCardDetailFetcher(any());
        assertNotNull(transaction.getTransactionCardHolderDetail());
        assertEquals("9411234567", transaction.getTransactionCardHolderDetail().getMobileNumber());
        assertEquals("v@gmail.com", transaction.getTransactionCardHolderDetail().getEmailId());
        assertEquals("Varun", transaction.getTransactionCardHolderDetail().getName());
    }
}
