package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardBlockedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailFetcherService;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardDetailsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code ExternalApiCardDetailFetcherServiceImpl} class is an implementation of the {@link
 * CardDetailFetcherService} interface specifically designed for the ACS (Access Control Server)
 * card detail retrieval functionality. This service is responsible for making calls to the external
 * API to retrieve card details and handle exceptions related to card retrieval and validation.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Slf4j
@Service(CardDetailsStore.CardStoreTypeConstants.EXTERNAL_API)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExternalApiCardDetailFetcherServiceImpl implements CardDetailFetcherService {
    @Override
    public CardDetailResponse getCardDetails(CardDetailsRequest cardDetailsRequest)
            throws CardDetailsNotFoundException {
        log.info("Fetching card details from External API");
        return null;
    }

    @Override
    public void blockCard(CardDetailsRequest cardDetailsRequest)
            throws CardDetailsNotFoundException {
        log.info("Block card details using External API");
    }

    public void validateCardDetails(CardDetailResponse cardDetailDto)
            throws CardBlockedException, CardDetailsNotFoundException {
        log.info("Validating card details from ACS");
    }
}
