package com.razorpay.ffs.cas.acs.service;

import java.util.stream.Stream;

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

import com.razorpay.ffs.cas.acs.exception.DataNotFoundException;
import com.razorpay.ffs.cas.acs.exception.InternalErrorCode;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSException;
import com.razorpay.ffs.cas.acs.service.impl.RangeServiceImpl;
import com.razorpay.ffs.cas.contract.enums.TransactionStatusReason;
import com.razorpay.ffs.cas.dao.enums.CardRangeStatus;
import com.razorpay.ffs.cas.dao.enums.InstitutionStatus;
import com.razorpay.ffs.cas.dao.enums.TransactionStatus;
import com.razorpay.ffs.cas.dao.model.CardRange;
import com.razorpay.ffs.cas.dao.model.CardRangeGroup;
import com.razorpay.ffs.cas.dao.model.Institution;
import com.razorpay.ffs.cas.dao.model.Network;
import com.razorpay.ffs.cas.dao.repository.CardRangeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RangeServiceTest {
    @Mock CardRangeRepository cardRangeRepository;
    @InjectMocks RangeServiceImpl rangeService;
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
        assertEquals("3007", exception.getMessage());
    }

    @Test
    public void testFindByPanNoCardRangeFound() throws Exception {
        when(cardRangeRepository.findByPan(Long.valueOf(PanNumber))).thenReturn(null);
        DataNotFoundException exception =
                assertThrows(
                        DataNotFoundException.class,
                        () -> rangeService.findByPan("4001400112341234"));
        assertEquals("3007", exception.getMessage());
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
                exception.getErrorCode().getTransactionStatusReason());
        assertEquals(
                TransactionStatus.UNABLE_TO_AUTHENTICATE,
                exception.getErrorCode().getTransactionStatus());
    }

    @Test
    public void validateRangeTest() throws ACSException, DataNotFoundException {
        CardRange cardRange = getCardRange(CardRangeStatus.ACTIVE, InstitutionStatus.ACTIVE);
        rangeService.validateRange(cardRange);
    }

    @ParameterizedTest
    @MethodSource("provideCardRangeNegative")
    public void validateCardRangeTestNegative(
            CardRange cardRange, InternalErrorCode internalErrorCode) {
        ACSException exception =
                assertThrows(ACSException.class, () -> rangeService.validateRange(cardRange));
        assertEquals(internalErrorCode, exception.getErrorCode());
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
                        InternalErrorCode.CARD_RANGE_NOT_ACTIVE),
                Arguments.of(
                        getCardRange(CardRangeStatus.ACTIVE, InstitutionStatus.INACTIVE),
                        InternalErrorCode.INSTITUTION_INACTIVE));
    }

    public static Stream<Arguments> provideCardRangeWithEmptyFields() {
        return Stream.of(
                Arguments.of(
                        getCardRangeWithEmpty(true, true, false),
                        InternalErrorCode.INVALID_NETWORK),
                Arguments.of(
                        getCardRangeWithEmpty(true, false, true),
                        InternalErrorCode.INSTITUTION_NOT_FOUND),
                Arguments.of(
                        getCardRangeWithEmpty(false, true, true),
                        InternalErrorCode.RANGE_GROUP_NOT_FOUND));
    }

    private static CardRange getCardRange(
            CardRangeStatus status, InstitutionStatus institutionStatus) {
        CardRange cardRange = new CardRange();
        CardRangeGroup cardRangeGroup = new CardRangeGroup();
        Institution institution = new Institution();
        cardRange.setCardRangeGroup(cardRangeGroup);
        cardRangeGroup.setInstitution(institution);
        cardRange.setStatus(status);
        institution.setStatus(institutionStatus);
        cardRange.setNetwork(Network.builder().code((byte) 1).build());
        return cardRange;
    }

    private static CardRange getCardRangeWithEmpty(
            Boolean hasCardRangeGroup, Boolean hasInstitution, boolean hasNetwork) {
        CardRange cardRange = new CardRange();
        CardRangeGroup cardRangeGroup = new CardRangeGroup();
        Institution institution = new Institution();
        cardRange.setStatus(CardRangeStatus.ACTIVE);
        institution.setStatus(InstitutionStatus.ACTIVE);

        if (hasInstitution) {
            cardRangeGroup.setInstitution(institution);
        }
        if (hasCardRangeGroup) {
            cardRange.setCardRangeGroup(cardRangeGroup);
        }
        if (hasNetwork) {
            cardRange.setNetwork(Network.builder().code((byte) 1).build());
        }
        return cardRange;
    }
}
