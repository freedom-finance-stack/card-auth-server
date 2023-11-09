package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CardDetailsMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailFetcherService;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardDetailsStore;
import org.freedomfinancestack.razorpay.cas.dao.model.CardDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.CardDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
@Service(CardDetailsStore.CardStoreTypeConstants.ACS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSCardDetailFetcherServiceImpl implements CardDetailFetcherService {
    private final CardDetailRepository cardDetailRepository;

    public CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException {
        log.info("Fetching card details from ACS");
        try {
            CardDetail cardDetail =
                    cardDetailRepository.findByCardNumber(cardDetailsRequest.getCardNumber());
            if (cardDetail != null) {
                return CardDetailResponse.builder()
                        .cardDetailDto(CardDetailsMapper.INSTANCE.toCardDetailDto(cardDetail))
                        .isSuccess(true)
                        .build();
            }
        } catch (DataAccessException ex) {
            throw new ACSDataAccessException(InternalErrorCode.CARD_USER_FETCH_EXCEPTION, ex);
        }

        return CardDetailResponse.builder().isSuccess(false).build();
    }

    @Override
    public void blockCard(CardDetailsRequest cardDetailsRequest) throws ACSDataAccessException {
        log.info("Block card in CardDetails table");
        try {
            cardDetailRepository.blockCard(
                    cardDetailsRequest.getCardNumber(), cardDetailsRequest.getInstitutionId());
        } catch (DataAccessException ex) {
            throw new ACSDataAccessException(InternalErrorCode.CARD_USER_FETCH_EXCEPTION, ex);
        }
    }

    public void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws CardBlockedException, CardDetailsNotFoundException {
        if (!cardDetailResponse.isSuccess() || cardDetailResponse.getCardDetailDto() == null) {
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
