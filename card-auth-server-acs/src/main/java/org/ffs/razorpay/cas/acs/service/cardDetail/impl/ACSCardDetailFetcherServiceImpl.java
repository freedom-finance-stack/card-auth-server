package org.ffs.razorpay.cas.acs.service.cardDetail.impl;

import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.dto.mapper.CardDetailsMapper;
import org.ffs.razorpay.cas.acs.exception.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.checked.CardBlockedException;
import org.ffs.razorpay.cas.acs.service.cardDetail.CardDetailFetcherService;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.ffs.razorpay.cas.dao.enums.CardDetailsStore;
import org.ffs.razorpay.cas.dao.model.CardDetail;
import org.ffs.razorpay.cas.dao.repository.CardDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
