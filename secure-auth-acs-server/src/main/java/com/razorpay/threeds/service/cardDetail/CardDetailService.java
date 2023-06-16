package com.razorpay.threeds.service.cardDetail;

import com.razorpay.acs.dao.contract.CardDetailsRequest;
import com.razorpay.acs.dao.enums.CardStoreType;
import com.razorpay.threeds.dto.CardDetailDto;
import com.razorpay.threeds.exception.UserBlockedException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.exception.checked.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardDetailService {

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final CardDetailFetcherFactory cardDetailFetcherFactory;

    public CardDetailDto getCardDetails(CardDetailsRequest cardDetailsRequest, CardStoreType type) throws ACSException {
        CardDetailFetcherService cardDetailFetcherService = cardDetailFetcherFactory.getCardDetailFetcher(type);
        return cardDetailFetcherService.getCardDetails(cardDetailsRequest);
    }

    public void validateCardDetails(CardDetailDto cardDetailDto) throws ACSException {
//        if (!cardDetailDto.isSuccess()) {
//            throw new ACSDataAccessException(ErrorCode.INSTITUTION_NOT_FOUND.getErrorCode(),
//                    "Invalid response from card fetcher");
//        }
//        if (cardDetailDto.isBlocked() || !cardDetailDto.isEnrolled()) {
//            throw new UserBlockedException(ErrorCode.INSTITUTION_NOT_FOUND(),
//                    "Card Number is blocked");
//        }

    }

}
