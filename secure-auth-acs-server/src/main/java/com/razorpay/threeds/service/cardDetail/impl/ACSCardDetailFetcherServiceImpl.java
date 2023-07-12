package com.razorpay.threeds.service.cardDetail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.acs.dao.enums.CardDetailsStore;
import com.razorpay.acs.dao.model.CardDetail;
import com.razorpay.acs.dao.repository.CardDetailRepository;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.dto.mapper.CardDetailsMapper;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.InternalErrorCode;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.CardBlockedException;
import com.razorpay.threeds.service.cardDetail.CardDetailFetcherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws DataNotFoundException, CardBlockedException {
        if (!cardDetailResponse.isSuccess() || cardDetailResponse.getCardDetailDto() == null) {
            log.error("Card Number not found");
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.CARD_USER_NOT_FOUND);
        } else if (cardDetailResponse.getCardDetailDto().isBlocked()) {
            throw new CardBlockedException(
                    InternalErrorCode.CARD_USER_BLOCKED, "Card Number is blocked");
        }
    }
}
