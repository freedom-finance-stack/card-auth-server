package com.razorpay.threeds.service.impl;


import com.razorpay.acs.dao.enums.CardRangeStatus;
import com.razorpay.acs.dao.enums.InstitutionStatus;
import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.acs.dao.model.Institution;
import com.razorpay.acs.dao.model.RangeGroup;
import com.razorpay.acs.dao.repository.CardRangeRepository;
import com.razorpay.threeds.exception.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.exception.DataNotFoundException;
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
            throw new DataNotFoundException(ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.CARD_RANGE_NOT_FOUND.getDefaultErrorMessage());
        }
        return cardRange;
    }

    public void validateRange(CardRange cardRange) throws ACSException {
        RangeGroup rangeGroup = cardRange.getRangeGroup();
        Institution institution =  rangeGroup.getInstitution();

        if (cardRange.getStatus() != CardRangeStatus.ACTIVE) {
            throw new ACSException(ErrorCode.CARD_RANGE_NOT_ACTIVE.getCode(), ErrorCode.CARD_RANGE_NOT_ACTIVE.getDefaultErrorMessage());
        }
        if (rangeGroup == null){
            throw new DataNotFoundException(ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.RANGE_GROUP_NOT_FOUND.getDefaultErrorMessage());
        }
        if (institution == null){
            throw new DataNotFoundException(ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.INSTITUTION_NOT_FOUND.getDefaultErrorMessage());
        }
        if (institution.getStatus() != InstitutionStatus.ACTIVE){
            throw new ACSException(ErrorCode.INSTITUTION_INACTIVE.getCode(),  ErrorCode.INSTITUTION_INACTIVE.getDefaultErrorMessage());
        }
    }
}
