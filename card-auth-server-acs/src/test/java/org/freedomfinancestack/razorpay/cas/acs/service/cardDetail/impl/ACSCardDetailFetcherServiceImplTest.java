package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.dao.model.CardDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.CardDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ACSCardDetailFetcherServiceImplTest {
    @Test
    public void getCardDetails() throws CardDetailsNotFoundException {
        // Arrange
        CardDetailRepository cardDetailRepository = mock(CardDetailRepository.class);
        CardDetail cardDetail =
                CardDetail.builder()
                        .id("1")
                        .institutionId("123")
                        .cardNumber("1234567890")
                        .cardExpiry("12/23")
                        .blocked(false)
                        .build();
        when(cardDetailRepository.findByCardNumber(anyString())).thenReturn(cardDetail);
        ACSCardDetailFetcherServiceImpl cardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(cardDetailRepository);
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("123", "1234567890");

        // Act
        CardDetailResponse result = cardDetailFetcherService.getCardDetails(cardDetailsRequest);

        // Assert
        assertTrue(result.isSuccess());
        assertNotNull(result.getCardDetailDto());
        assertEquals(cardDetail.getCardNumber(), result.getCardDetailDto().getCardNumber());
        assertEquals(cardDetail.getCardExpiry(), result.getCardDetailDto().getCardExpiry());
    }

    @Test
    public void getCardDetails_exception() throws CardDetailsNotFoundException {
        // Arrange
        CardDetailRepository cardDetailRepository = mock(CardDetailRepository.class);
        when(cardDetailRepository.findByCardNumber(anyString())).thenReturn(null);
        ACSCardDetailFetcherServiceImpl cardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(cardDetailRepository);
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("123", "1234567890");

        assertFalse(cardDetailFetcherService.getCardDetails(cardDetailsRequest).isSuccess());
    }

    @Test
    public void getCardDetails_exception_DataAccessException() throws CardDetailsNotFoundException {
        CardDetailRepository cardDetailRepository = mock(CardDetailRepository.class);
        ACSCardDetailFetcherServiceImpl cardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(cardDetailRepository);
        when(cardDetailRepository.findByCardNumber(anyString()))
                .thenThrow(new DataAccessException("Mocked Exception") {});
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("123", "1234567890");

        CardDetailsNotFoundException cardDetailsNotFoundException =
                assertThrows(
                        CardDetailsNotFoundException.class,
                        () -> cardDetailFetcherService.getCardDetails(cardDetailsRequest));

        assertEquals(
                InternalErrorCode.CARD_USER_FETCH_EXCEPTION,
                cardDetailsNotFoundException.getErrorCode());
    }

    @Test
    public void blockCard_success_case() throws CardDetailsNotFoundException {
        CardDetailRepository cardDetailRepositoryMock = Mockito.mock(CardDetailRepository.class);

        ACSCardDetailFetcherServiceImpl cardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(cardDetailRepositoryMock);

        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("institutionId", "12");

        cardDetailFetcherService.blockCard(cardDetailsRequest);

        Mockito.verify(cardDetailRepositoryMock).blockCard(anyString(), anyString());
    }

    @Test
    public void blockCard_Exception_DataAccessException() {
        CardDetailRepository cardDetailRepository = mock(CardDetailRepository.class);
        ACSCardDetailFetcherServiceImpl cardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(cardDetailRepository);
        doThrow(new DataAccessException("Mocked Exception") {})
                .when(cardDetailRepository)
                .blockCard(anyString(), anyString());
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("123", "1234567890");

        CardDetailsNotFoundException cardDetailsNotFoundException =
                assertThrows(
                        CardDetailsNotFoundException.class,
                        () -> cardDetailFetcherService.blockCard(cardDetailsRequest));

        assertEquals(
                InternalErrorCode.CARD_USER_FETCH_EXCEPTION,
                cardDetailsNotFoundException.getErrorCode());
    }

    @Test
    public void validateCardDetails_Success() {
        CardDetailResponse cardDetailResponse =
                CardDetailResponse.builder()
                        .cardDetailDto(CardDetailDto.builder().blocked(true).build())
                        .isSuccess(true)
                        .build();

        ACSCardDetailFetcherServiceImpl acsCardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(mock(CardDetailRepository.class));

        assertThrows(
                CardBlockedException.class,
                () -> acsCardDetailFetcherService.validateCardDetails(cardDetailResponse));
    }

    @Test
    public void validateCardDetails_Exception() {

        ACSCardDetailFetcherServiceImpl acsCardDetailFetcherService =
                new ACSCardDetailFetcherServiceImpl(mock(CardDetailRepository.class));
        CardDetailResponse cardDetailResponse = null;
        assertThrows(
                CardDetailsNotFoundException.class,
                () -> acsCardDetailFetcherService.validateCardDetails(cardDetailResponse));
    }
}
