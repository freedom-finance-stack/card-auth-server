package com.razorpay.threeds.service.cardDetail;

import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.exception.checked.ACSException;

public interface CardDetailFetcherService {
    CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest) throws ACSException;
    void validateCardDetails(CardDetailResponse cardDetailResponse)  throws ACSException, ThreeDSException;
}
