package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MockCardDetailFetcherServiceImplTest {
    @Test
    public void test_success_case() throws CardDetailsNotFoundException {
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("I1", "1234567890123456");

        CardDetailResponse response =
                new MockCardDetailFetcherServiceImpl().getCardDetails(cardDetailsRequest);

        assertNotNull(response);

        assertTrue(response.isSuccess());

        assertNotNull(response.getCardDetailDto());

        assertEquals("1234567890123456", response.getCardDetailDto().getCardNumber());
        assertEquals("0525", response.getCardDetailDto().getCardExpiry());
        assertEquals("TEST", response.getCardDetailDto().getName());
        assertEquals("9999999999", response.getCardDetailDto().getMobileNumber());
        assertEquals("TEST@ffs.org", response.getCardDetailDto().getEmailId());
        assertEquals("01-01-2023", response.getCardDetailDto().getDob());
        assertFalse(response.getCardDetailDto().isBlocked());
    }

    @Test
    public void test_validates_card_detail_response_successfully() {
        // Arrange
        CardDetailResponse cardDetailResponse =
                CardDetailResponse.builder()
                        .cardDetailDto(
                                CardDetailDto.builder().cardNumber("1234567890123456").build())
                        .isSuccess(true)
                        .build();

        MockCardDetailFetcherServiceImpl mockCardDetailFetcherService =
                new MockCardDetailFetcherServiceImpl();

        // Act and Assert
        assertDoesNotThrow(
                () -> mockCardDetailFetcherService.validateCardDetails(cardDetailResponse));
    }

    @ParameterizedTest
    @CsvSource({
        "7654320599999998, TEST_TRANSACTION_FAILED",
        "7654360899999998, TEST_TRANSACTION_REJECTED",
        "7654340699999998, TEST_TRANSACTION_UA"
    })
    public void validateCardDetails_Exceptions(String cardNumber, String errorCode) {
        // Arrange
        CardDetailResponse cardDetailResponse =
                CardDetailResponse.builder()
                        .cardDetailDto(CardDetailDto.builder().cardNumber(cardNumber).build())
                        .isSuccess(true)
                        .build();

        MockCardDetailFetcherServiceImpl mockCardDetailFetcherService =
                new MockCardDetailFetcherServiceImpl();

        // Act and Assert
        CardDetailsNotFoundException exception =
                assertThrows(
                        CardDetailsNotFoundException.class,
                        () -> mockCardDetailFetcherService.validateCardDetails(cardDetailResponse));
        assertEquals(
                InternalErrorCode.valueOf(String.valueOf(errorCode)), exception.getErrorCode());
    }

    @Test
    public void validateCardDetails_Exceptions_isSuccess_False() {
        // Arrange
        CardDetailResponse cardDetailResponse =
                CardDetailResponse.builder()
                        .cardDetailDto(
                                CardDetailDto.builder().cardNumber("7554340600000001").build())
                        .isSuccess(false)
                        .build();

        MockCardDetailFetcherServiceImpl mockCardDetailFetcherService =
                new MockCardDetailFetcherServiceImpl();

        // Act and Assert
        CardDetailsNotFoundException exception =
                assertThrows(
                        CardDetailsNotFoundException.class,
                        () -> mockCardDetailFetcherService.validateCardDetails(cardDetailResponse));
        assertEquals(InternalErrorCode.CARD_USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void validateCardDetails_Exceptions_isSuccess_true() {
        // Arrange
        CardDetailResponse cardDetailResponse =
                CardDetailResponse.builder()
                        .cardDetailDto(
                                CardDetailDto.builder()
                                        .cardNumber("7554340600000001")
                                        .blocked(true)
                                        .build())
                        .isSuccess(true)
                        .build();

        MockCardDetailFetcherServiceImpl mockCardDetailFetcherService =
                new MockCardDetailFetcherServiceImpl();

        // Act and Assert
        CardBlockedException exception =
                assertThrows(
                        CardBlockedException.class,
                        () -> mockCardDetailFetcherService.validateCardDetails(cardDetailResponse));
        assertEquals(InternalErrorCode.CARD_USER_BLOCKED, exception.getErrorCode());
    }
}
