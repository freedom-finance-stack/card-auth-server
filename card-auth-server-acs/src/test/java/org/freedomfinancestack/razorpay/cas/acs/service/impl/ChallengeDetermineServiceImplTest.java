package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.CardholderAccountInformation;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSReqAuthMethodInd;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSRequestorChallengeInd;
import org.freedomfinancestack.razorpay.cas.dao.enums.RiskFlag;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeDetermineServiceImplTest {

    @Mock private AREQ mockAREQ;

    @InjectMocks private ChallengeDetermineServiceImpl challengeDetermineService;

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForChallengeRequestedMandate() {

        Transaction transaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_MANDATE.getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(mockAREQ, transaction, RiskFlag.RBA);

        assertTrue(transaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, transaction.getTransactionStatus());
    }

    @Test
    void
            determineChallenge_shouldSetChallengeMandatedAndStatusForChallengeRequestedRequesterPreference() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(
                        ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_REQUESTER_PREFERENCE
                                .getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForWhitelistPrompt() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(
                        ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForTransactionalRiskAnalysis() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(
                        ThreeDSRequestorChallengeInd
                                .TRANSACTIONAL_RISK_ANALYSIS_IS_ALREADY_PERFORMED
                                .getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertFalse(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.SUCCESS, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForDataShareOnly() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.DATA_SHARE_ONLY.getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertFalse(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.SUCCESS, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForFailedAuthMethod() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.DATA_SHARE_ONLY.getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(
                mockAREQ, mockTransaction, RiskFlag.INFORMATIONAL);

        assertFalse(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.INFORMATIONAL, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForDecoupledChallenge() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.NO_PREFERENCE.getValue());
        when(mockAREQ.getThreeDSRequestorDecReqInd()).thenReturn(InternalConstants.YES);
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(
                mockAREQ, mockTransaction, RiskFlag.DECOUPLED_CHALLENGE);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(
                TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED,
                mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForDecoupledChallenge1() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.NO_PREFERENCE.getValue());
        when(mockAREQ.getThreeDSRequestorDecReqInd()).thenReturn(InternalConstants.YES);
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.CHALLENGE);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForDecoupledChallenge2() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.NO_PREFERENCE.getValue());
        when(mockAREQ.getThreeDSRequestorDecReqInd()).thenReturn(InternalConstants.YES);
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        challengeDetermineService.determineChallenge(
                mockAREQ, mockTransaction, RiskFlag.NO_CHALLENGE);

        assertFalse(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.SUCCESS, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetInformationalStatus() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd()).thenReturn("anyOtherValue");

        challengeDetermineService.determineChallenge(
                mockAREQ, mockTransaction, RiskFlag.INFORMATIONAL);

        assertFalse(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.INFORMATIONAL, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetNoChallengeStatus() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd()).thenReturn(null);

        challengeDetermineService.determineChallenge(
                mockAREQ, mockTransaction, RiskFlag.NO_CHALLENGE);

        assertFalse(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.SUCCESS, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetChallengeMandatedAndStatusForSuspiciousAccountActivity() {
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.NO_PREFERENCE.getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd()).thenReturn("anyMethod");

        CardholderAccountInformation acctInfo = new CardholderAccountInformation();
        acctInfo.setSuspiciousAccActivity("02");
        when(mockAREQ.getAcctInfo()).thenReturn(acctInfo);

        Transaction mockTransaction = new Transaction();
        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldSetRBA() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd()).thenReturn(null);

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldThreeDSReqAuthMethodIndFAILEDSetChallenge() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.NO_PREFERENCE.getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd())
                .thenReturn(ThreeDSReqAuthMethodInd.FAILED.getValue());

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }

    @Test
    void determineChallenge_shouldThreeDSReqAuthMethodIndNOT_PERFORMEDSetChallenge() {
        Transaction mockTransaction = new Transaction();
        when(mockAREQ.getThreeDSRequestorChallengeInd())
                .thenReturn(ThreeDSRequestorChallengeInd.NO_PREFERENCE.getValue());
        when(mockAREQ.getThreeDSReqAuthMethodInd())
                .thenReturn(ThreeDSReqAuthMethodInd.NOT_PERFORMED.getValue());

        challengeDetermineService.determineChallenge(mockAREQ, mockTransaction, RiskFlag.RBA);

        assertTrue(mockTransaction.isChallengeMandated());
        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, mockTransaction.getTransactionStatus());
    }
}
