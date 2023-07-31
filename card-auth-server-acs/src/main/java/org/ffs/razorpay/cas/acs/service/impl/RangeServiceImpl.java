package org.ffs.razorpay.cas.acs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.ffs.razorpay.cas.acs.service.RangeService;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.ffs.razorpay.cas.dao.enums.CardRangeStatus;
import org.ffs.razorpay.cas.dao.enums.InstitutionStatus;
import org.ffs.razorpay.cas.dao.model.CardRange;
import org.ffs.razorpay.cas.dao.model.CardRangeGroup;
import org.ffs.razorpay.cas.dao.model.Institution;
import org.ffs.razorpay.cas.dao.repository.CardRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RangeServiceImpl implements RangeService {
    private final CardRangeRepository cardRangeRepository;

    public CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException {
        if (StringUtils.isBlank(pan)) {
            log.error("PAN is null or empty");
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.CARD_RANGE_NOT_FOUND);
        }

        CardRange cardRange;
        try {
            cardRange = cardRangeRepository.findByPan(Long.valueOf(pan));
        } catch (DataAccessException e) {
            log.error(
                    "Error while fetching card range for PAN: "
                            + pan); // todo Noncompliance : Mask PAN
            throw new ACSDataAccessException(InternalErrorCode.CARD_RANGE_FETCH_EXCEPTION, e);
        }

        if (cardRange == null) {
            log.error("Card range not found for PAN: " + pan); // todo Noncompliance : Mask PAN
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.CARD_RANGE_NOT_FOUND);
        }

        return cardRange;
    }

    public void validateRange(CardRange cardRange)
            throws TransactionDataNotValidException, DataNotFoundException {
        if (cardRange.getStatus() != CardRangeStatus.ACTIVE) {
            throw new TransactionDataNotValidException(InternalErrorCode.CARD_RANGE_NOT_ACTIVE);
        }

        CardRangeGroup cardRangeGroup = cardRange.getCardRangeGroup();
        if (cardRangeGroup == null) {
            log.error("Card range group not found for card range: " + cardRange.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.RANGE_GROUP_NOT_FOUND);
        }

        Institution institution = cardRangeGroup.getInstitution();
        if (institution == null) {
            log.error("Institution not found for card range group: " + cardRangeGroup.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.INSTITUTION_NOT_FOUND);
        }
        if (institution.getStatus() != InstitutionStatus.ACTIVE) {
            throw new TransactionDataNotValidException(InternalErrorCode.INSTITUTION_INACTIVE);
        }

        if (cardRange.getNetwork() == null) {
            log.error("Network not found for card range: " + cardRange.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.INVALID_NETWORK);
        }
    }
}
