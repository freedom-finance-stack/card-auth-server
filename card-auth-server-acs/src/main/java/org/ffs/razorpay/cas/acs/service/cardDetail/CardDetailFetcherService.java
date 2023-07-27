package org.ffs.razorpay.cas.acs.service.cardDetail;

import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.exception.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.checked.CardBlockedException;

public interface CardDetailFetcherService {
    CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException;

    void validateCardDetails(CardDetailResponse cardDetailResponse)
            throws CardBlockedException, DataNotFoundException;
}
