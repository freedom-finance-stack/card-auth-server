package com.razorpay.threeds.service.cardDetail;

import com.razorpay.acs.dao.contract.CardDetailsRequest;
import com.razorpay.threeds.dto.CardDetailDto;
import com.razorpay.threeds.exception.checked.ACSException;

public interface CardDetailFetcherService {
   public CardDetailDto getCardDetails(CardDetailsRequest cardDetailsRequest) throws ACSException;
}
