package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.service.CardRangeService;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardRangeStatus;
import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.repository.CardRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code RangeServiceImpl} class is an implementation of the {@link CardRangeService} interface
 * that provides functionality to fetch card range details and validate card ranges based on
 * transaction data in the ACS (Access Control Server) system.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardRangeServiceImpl implements CardRangeService {
    private final CardRangeRepository cardRangeRepository;

    /**
     * Fetches card range for given PAN
     *
     * @param pan
     * @return The {@link CardRange} entity corresponding to the given primary key.
     * @throws DataNotFoundException
     * @throws ACSDataAccessException
     */
    public CardRange findByPan(String pan)
            throws DataNotFoundException, ACSDataAccessException, CardDetailsNotFoundException {
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
            throw new CardDetailsNotFoundException(
                    InternalErrorCode.CARD_RANGE_NOT_FOUND,
                    InternalErrorCode.RANGE_GROUP_NOT_FOUND.getDefaultErrorMessage());
        }

        return cardRange;
    }

    /**
     * Validates the CardRange entity based on the given transaction data.
     *
     * @param cardRange the {@link CardRange} entity to be validated.
     * @throws TransactionDataNotValidException
     * @throws DataNotFoundException
     */
    public void validateRange(CardRange cardRange)
            throws TransactionDataNotValidException,
                    DataNotFoundException,
                    CardDetailsNotFoundException {
        if (cardRange.getStatus() != CardRangeStatus.ACTIVE) {
            throw new CardDetailsNotFoundException(
                    InternalErrorCode.CARD_RANGE_NOT_ACTIVE,
                    InternalErrorCode.CARD_RANGE_NOT_ACTIVE.getDefaultErrorMessage());
        }

        Institution institution = cardRange.getInstitution();
        if (institution == null) {
            log.error("Institution not found for card range: " + cardRange.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.INSTITUTION_NOT_FOUND);
        }
        if (institution.getStatus() != InstitutionStatus.ACTIVE) {
            throw new TransactionDataNotValidException(InternalErrorCode.INSTITUTION_INACTIVE);
        }

        if (cardRange.getNetworkCode() == null || cardRange.getNetworkCode() == 0) {
            log.error("Network not found for card range: " + cardRange.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.INVALID_NETWORK);
        }
    }
}
