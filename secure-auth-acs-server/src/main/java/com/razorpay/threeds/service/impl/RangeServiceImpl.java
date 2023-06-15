package com.razorpay.threeds.service.impl;


import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.acs.dao.repository.CardRangeRepository;
import com.razorpay.threeds.exception.checked.DataNotFoundException;
import com.razorpay.threeds.exception.checked.ErrorCode;
import com.razorpay.threeds.service.RangeService;
import com.razorpay.threeds.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RangeServiceImpl implements RangeService {
    private final CardRangeRepository cardRangeRepository;

    public CardRange findByPan(String pan) throws DataNotFoundException {
        CardRange cardRange = cardRangeRepository.findByPan(Long.valueOf(pan));
        if (cardRange == null) {
            throw new DataNotFoundException(ErrorCode.CARD_RANGE_NOT_FOUND.getCode(),
                    "Card range not found for pan:" + Util.maskedCardNumber(pan));
        }
        return cardRange;
    }
}
