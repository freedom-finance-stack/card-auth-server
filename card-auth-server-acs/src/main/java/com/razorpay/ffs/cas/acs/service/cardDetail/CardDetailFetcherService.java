package com.razorpay.ffs.cas.acs.service.cardDetail;

import com.razorpay.ffs.cas.acs.dto.CardDetailResponse;
import com.razorpay.ffs.cas.acs.dto.CardDetailsRequest;
import com.razorpay.ffs.cas.acs.exception.DataNotFoundException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.exception.checked.CardBlockedException;

public interface CardDetailFetcherService {
    CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException;

    void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws CardBlockedException, DataNotFoundException;
}
