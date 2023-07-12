package com.razorpay.threeds.service.cardDetail;

import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.CardBlockedException;

public interface CardDetailFetcherService {
    CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException;

    void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws CardBlockedException, DataNotFoundException;
}
