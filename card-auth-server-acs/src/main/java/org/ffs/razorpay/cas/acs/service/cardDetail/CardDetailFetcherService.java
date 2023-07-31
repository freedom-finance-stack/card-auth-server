package org.ffs.razorpay.cas.acs.service.cardDetail;

import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;

public interface CardDetailFetcherService {
    CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException;

    void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws CardBlockedException, DataNotFoundException;
}
