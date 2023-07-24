package com.razorpay.ffs.cas.acs.service.cardDetail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.dto.CardDetailResponse;
import com.razorpay.ffs.cas.acs.dto.CardDetailsRequest;
import com.razorpay.ffs.cas.acs.exception.DataNotFoundException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.exception.checked.CardBlockedException;
import com.razorpay.ffs.cas.acs.service.cardDetail.CardDetailFetcherService;
import com.razorpay.ffs.cas.dao.enums.CardDetailsStore;

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
