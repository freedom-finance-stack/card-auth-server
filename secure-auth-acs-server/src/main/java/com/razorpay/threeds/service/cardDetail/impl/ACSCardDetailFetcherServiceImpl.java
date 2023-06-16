package com.razorpay.threeds.service.cardDetail.impl;


import com.razorpay.acs.dao.contract.CardDetailsRequest;
import com.razorpay.acs.dao.enums.CardStoreType;
import com.razorpay.acs.dao.repository.CardRangeRepository;
import com.razorpay.threeds.dto.CardDetailDto;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.cardDetail.CardDetailFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(CardStoreType.CardStoreTypeConstants.ACS)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSCardDetailFetcherServiceImpl implements CardDetailFetcherService {
    private final CardRangeRepository cardRangeRepository;
    public CardDetailDto getCardDetails(CardDetailsRequest cardDetailsRequest) throws ACSException {
        log.info("Fetching card details from ACS");
        return null;
    }
}
