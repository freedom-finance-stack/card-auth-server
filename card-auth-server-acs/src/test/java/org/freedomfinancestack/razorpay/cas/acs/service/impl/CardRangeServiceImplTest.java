package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.stream.Stream;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.CardRangeStatus;
import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.repository.CardRangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardRangeServiceImplTest {
    @Mock CardRangeRepository cardRangeRepository;
    @InjectMocks CardRangeServiceImpl rangeService;
    static String PanNumber = "4001400112341234";

    @Test
    public void testFindByPan() throws Exception {
        when(cardRangeRepository.findByPan(Long.valueOf(PanNumber)))
                .thenReturn(getCardRange(CardRangeStatus.ACTIVE, InstitutionStatus.ACTIVE));
        CardRange cardRange = rangeService.findByPan(PanNumber);
        assertNotNull(cardRange);
        assertEquals(getCardRange(CardRangeStatus.ACTIVE, InstitutionStatus.ACTIVE), cardRange);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testFindByPanEmptyValue(String input) {
        DataNotFoundException exception =
                assertThrows(DataNotFoundException.class, () -> rangeService.findByPan(input));
        assertEquals("1007 : CARD RANGE NOT FOUND", exception.getMessage());
    }

    @Test
    public void testFindByPanNoCardRangeFound() throws Exception {
        when(cardRangeRepository.findByPan(Long.valueOf(PanNumber))).thenReturn(null);
        DataNotFoundException exception =
                assertThrows(
                        DataNotFoundException.class,
                        () -> rangeService.findByPan("4001400112341234"));
        assertEquals("305", exception.getThreeDSecureErrorCode().getErrorCode());
    }

    @Test
    public void testFindByPanDataAccessException() throws Exception {
        when(cardRangeRepository.findByPan(Long.valueOf(PanNumber)))
                .thenThrow(new DataRetrievalFailureException("DataAccessException"));
        ACSDataAccessException exception =
                assertThrows(
                        ACSDataAccessException.class,
                        () -> rangeService.findByPan("4001400112341234"));
        assertEquals("Error while fetching card range details", exception.getMessage());
        assertEquals(
                TransactionStatusReason.ACS_TECHNICAL_ISSUE,
                exception.getInternalErrorCode().getTransactionStatusReason());
        assertEquals(
                TransactionStatus.UNABLE_TO_AUTHENTICATE,
                exception.getInternalErrorCode().getTransactionStatus());
    }

    @Test
    public void validateRangeTest()
            throws TransactionDataNotValidException,
                    DataNotFoundException,
                    CardDetailsNotFoundException {
        CardRange cardRange = getCardRange(CardRangeStatus.ACTIVE, InstitutionStatus.ACTIVE);
        rangeService.validateRange(cardRange);
    }

    @ParameterizedTest
    @MethodSource("provideCardRangeNegative")
    public void validateCardRangeTestNegative(
            CardRange cardRange, InternalErrorCode internalErrorCode) {
        CardDetailsNotFoundException exception =
                assertThrows(
                        CardDetailsNotFoundException.class,
                        () -> rangeService.validateRange(cardRange));
        assertEquals(internalErrorCode, exception.getErrorCode());
    }

    @Test
    public void validateCardRangeTestNegative() {
        TransactionDataNotValidException exception =
                assertThrows(
                        TransactionDataNotValidException.class,
                        () ->
                                rangeService.validateRange(
                                        getCardRange(
                                                CardRangeStatus.ACTIVE,
                                                InstitutionStatus.INACTIVE)));
        assertEquals(InternalErrorCode.INSTITUTION_INACTIVE, exception.getInternalErrorCode());
    }

    @ParameterizedTest
    @MethodSource("provideCardRangeWithEmptyFields")
    public void validateCardRangeTestWithEmptyFields(
            CardRange cardRange, InternalErrorCode internalErrorCode) {
        DataNotFoundException exception =
                assertThrows(
                        DataNotFoundException.class, () -> rangeService.validateRange(cardRange));
        assertEquals(internalErrorCode, exception.getInternalErrorCode());
    }

    public static Stream<Arguments> provideCardRangeNegative() {
        return Stream.of(
                Arguments.of(
                        getCardRange(CardRangeStatus.INACTIVE, InstitutionStatus.ACTIVE),
                        InternalErrorCode.CARD_RANGE_NOT_ACTIVE));
    }

    public static Stream<Arguments> provideCardRangeWithEmptyFields() {
        return Stream.of(
                Arguments.of(getCardRangeWithEmpty(true, false), InternalErrorCode.INVALID_NETWORK),
                Arguments.of(
                        getCardRangeWithEmpty(false, true),
                        InternalErrorCode.INSTITUTION_NOT_FOUND));
    }

    private static CardRange getCardRange(
            CardRangeStatus status, InstitutionStatus institutionStatus) {
        CardRange cardRange = new CardRange();
        Institution institution = new Institution();
        cardRange.setInstitution(institution);
        cardRange.setStatus(status);
        institution.setStatus(institutionStatus);
        cardRange.setNetworkCode((byte) 1);
        return cardRange;
    }

    private static CardRange getCardRangeWithEmpty(Boolean hasInstitution, boolean hasNetwork) {
        CardRange cardRange = new CardRange();
        Institution institution = new Institution();
        cardRange.setStatus(CardRangeStatus.ACTIVE);
        institution.setStatus(InstitutionStatus.ACTIVE);

        if (hasInstitution) {
            cardRange.setInstitution(institution);
        }
        if (hasNetwork) {
            cardRange.setNetworkCode((byte) 1);
        }
        return cardRange;
    }
}
