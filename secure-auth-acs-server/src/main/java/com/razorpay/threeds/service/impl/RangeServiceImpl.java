package com.razorpay.threeds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.CardRangeStatus;
import com.razorpay.acs.dao.enums.InstitutionStatus;
import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.acs.dao.model.CardRangeGroup;
import com.razorpay.acs.dao.model.Institution;
import com.razorpay.acs.dao.repository.CardRangeRepository;
import com.razorpay.threeds.exception.DataNotFoundException;
import com.razorpay.threeds.exception.ErrorCode;
import com.razorpay.threeds.exception.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.RangeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RangeServiceImpl implements RangeService {
  private final CardRangeRepository cardRangeRepository;

  public CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException {
    CardRange cardRange = null;
    try {
      cardRange = cardRangeRepository.findByPan(Long.valueOf(pan));
    } catch (DataAccessException e) {
      log.error("Error while fetching card range for PAN: " + pan); // todo Noncompliance : Mask PAN
      throw new ACSDataAccessException(ErrorCode.CARD_RANGE_FETCH_EXCEPTION, e);
    }

    if (cardRange == null) {
      log.error("Card range not found for PAN: " + pan); // todo Noncompliance : Mask PAN
      throw new DataNotFoundException(
          ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.CARD_RANGE_NOT_FOUND);
    }
    return cardRange;
  }

  public void validateRange(CardRange cardRange) throws ACSException, DataNotFoundException {
    CardRangeGroup cardRangeGroup = cardRange.getCardRangeGroup();
    Institution institution = cardRangeGroup.getInstitution();

    if (cardRange.getStatus() != CardRangeStatus.ACTIVE) {
      throw new ACSException(ErrorCode.CARD_RANGE_NOT_ACTIVE);
    }
    if (cardRangeGroup == null) {
      log.error("Card range group not found for card range: " + cardRange.getId());
      throw new DataNotFoundException(
          ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.RANGE_GROUP_NOT_FOUND);
    }
    if (institution == null) {
      log.error("Institution not found for card range group: " + cardRangeGroup.getId());
      throw new DataNotFoundException(
          ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.INSTITUTION_NOT_FOUND);
    }
    if (institution.getStatus() != InstitutionStatus.ACTIVE) {
      throw new ACSException(ErrorCode.INSTITUTION_INACTIVE);
    }
    if (cardRange.getNetwork() == null) {
      log.error("Network not found for card range: " + cardRange.getId());
      throw new DataNotFoundException(
          ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE, ErrorCode.INVALID_NETWORK);
    }
  }
}
