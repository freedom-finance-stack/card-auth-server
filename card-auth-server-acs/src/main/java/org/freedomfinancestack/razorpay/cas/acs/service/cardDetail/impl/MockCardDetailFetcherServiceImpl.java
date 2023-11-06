package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailFetcherService;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardDetailsStore;
import org.freedomfinancestack.razorpay.cas.dao.repository.CardDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code ACSCardDetailFetcherServiceImpl} class is an implementation of the {@link
 * CardDetailFetcherService} interface specifically designed for the ACS (Access Control Server)
 * card detail retrieval functionality. This service is responsible for fetching card details from
 * the ACS system, validating the card data, and handling exceptions related to card retrieval and
 * validation.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Slf4j
@Service(CardDetailsStore.CardStoreTypeConstants.MOCK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MockCardDetailFetcherServiceImpl implements CardDetailFetcherService {
    private final CardDetailRepository cardDetailRepository;

    public CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException {
        log.info("Fetching card details from Mock Implementation");
        CardDetailDto cardDto =
                CardDetailDto.builder()
                        .institutionId("I1")
                        .cardExpiry("0525")
                        .cardNumber(cardDetailsRequest.getCardNumber())
                        .mobileNumber("9999999999")
                        .name("TEST")
                        .emailId("TEST@ffs.org")
                        .dob("01-01-2023")
                        .blocked(false)
                        .build();

        long cardNumber = Long.parseLong(cardDetailsRequest.getCardNumber());
        if (cardNumber >= 7654340600000000L && cardNumber <= 7654340699999999L) { // No Card found
            CardDetailResponse.builder()
                    .isSuccess(false)
                    .cardDetailDto(new CardDetailDto())
                    .build();
        } else if (cardNumber >= 7654320500000000L
                && cardNumber <= 7654320599999999L) { // card Blocked
            cardDto.setBlocked(true);
        }

        return CardDetailResponse.builder().isSuccess(true).cardDetailDto(cardDto).build();
    }

    @Override
    public void blockCard(CardDetailsRequest cardDetailsRequest) throws ACSDataAccessException {
        log.info("Blocked card Mocked");
    }

    public void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws DataNotFoundException, CardBlockedException, CardDetailsNotFoundException {
        if (cardDetailResponse == null
                || !cardDetailResponse.isSuccess()
                || cardDetailResponse.getCardDetailDto() == null) {
            log.error("Card Number not found");
            throw new CardDetailsNotFoundException(
                    InternalErrorCode.CARD_USER_NOT_FOUND,
                    InternalErrorCode.CARD_USER_NOT_FOUND.getDefaultErrorMessage());
        } else if (cardDetailResponse.getCardDetailDto().isBlocked()) {
            throw new CardBlockedException(
                    InternalErrorCode.CARD_USER_BLOCKED, "Card Number is blocked");
        }
    }
}
