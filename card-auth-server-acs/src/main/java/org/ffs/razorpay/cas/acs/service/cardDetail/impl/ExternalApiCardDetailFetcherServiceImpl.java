package org.ffs.razorpay.cas.acs.service.cardDetail.impl;

import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.acs.service.cardDetail.CardDetailFetcherService;
import org.ffs.razorpay.cas.dao.enums.CardDetailsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(CardDetailsStore.CardStoreTypeConstants.EXTERNAL_API)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExternalApiCardDetailFetcherServiceImpl implements CardDetailFetcherService {
    @Override
    public CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws ACSDataAccessException {
        log.info("Fetching card details from External API");
        return null;
    }

    public void validateCardDetails(CardDetailResponse cardDetailDto)
            throws CardBlockedException, DataNotFoundException {
        log.info("Validating card details from ACS");
    }
}
