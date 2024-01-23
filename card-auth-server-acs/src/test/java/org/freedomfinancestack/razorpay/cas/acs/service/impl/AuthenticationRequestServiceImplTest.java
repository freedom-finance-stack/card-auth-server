package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.AResMapperImpl;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.HelperMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.DecoupledAuthenticationAsyncService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.locator.TransactionTimeoutServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.validation.ThreeDSValidator;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.TestUtil.setPrivateField;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthenticationRequestServiceImplTest {

    @Mock private TransactionService transactionService;

    @Mock private TransactionMessageLogService transactionMessageLogService;

    @Mock private CardRangeService cardRangeService;

    @Mock private CardDetailService cardDetailService;

    @Mock private AuthValueGeneratorService authValueGeneratorService;

    private AResMapperImpl aResMapper;

    @Mock private TransactionTimeoutServiceLocator transactionTimeoutServiceLocator;

    @Mock private DecoupledAuthenticationAsyncService decoupledAuthenticationAsyncService;

    @Mock private FeatureService featureService;

    @Mock private SignerService signerService;

    @Mock private ChallengeDetermineService challengeDetermineService;

    @Mock private Environment environment;

    @Mock private TestConfigProperties testConfigProperties;

    private AppConfiguration appConfiguration = TransactionTestData.createSampleAcsConfig();

    @Mock private ThreeDSValidator<AREQ> areqValidator;

    private AuthenticationRequestServiceImpl authenticationRequestService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        HelperMapper helperMapper = new HelperMapper();
        helperMapper.setAppConfiguration(appConfiguration);
        aResMapper = new AResMapperImpl();
        setPrivateField(aResMapper, "helperMapper", helperMapper);
        authenticationRequestService =
                new AuthenticationRequestServiceImpl(
                        transactionService,
                        transactionMessageLogService,
                        cardRangeService,
                        cardDetailService,
                        authValueGeneratorService,
                        aResMapper,
                        transactionTimeoutServiceLocator,
                        decoupledAuthenticationAsyncService,
                        featureService,
                        signerService,
                        challengeDetermineService,
                        environment,
                        testConfigProperties,
                        appConfiguration,
                        areqValidator);
    }

    @Test
    void processAuthenticationRequest_SuccessfullyProcessesBRW()
            throws ThreeDSException, ACSException {
        // Given
        AREQ validAReq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        when(transactionService.create(any())).thenReturn(transaction);
        doNothing().when(areqValidator).validateRequest(validAReq);
        when(cardRangeService.findByPan(any()))
                .thenReturn(TransactionTestData.createSampleCardRange());
        doNothing().when(cardRangeService).validateRange(any());
        doNothing().when(cardDetailService).validateAndUpdateCardDetails(any(), any(), any());
        doNothing().when(featureService).getACSRenderingType(any(), any());
        doNothing().when(challengeDetermineService).determineChallenge(any(), any(), any());
        when(transactionService.saveOrUpdate(any())).thenReturn(transaction);
        when(authValueGeneratorService.getAuthValue(any())).thenReturn("MOCK_AUTH_VAL");
        when(environment.getActiveProfiles()).thenReturn(new String[] {"test"});

        doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            Transaction trans = (Transaction) args[1];
                            // Perform your updates on the transaction object
                            trans.setTransactionStatus(TransactionStatus.SUCCESS);
                            trans.setChallengeMandated(false);
                            return null;
                        })
                .when(challengeDetermineService)
                .determineChallenge(any(), any(), any());

        doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            Transaction trans = (Transaction) args[0];
                            // Perform your updates on the transaction object
                            trans.setEci(ECI.VISA_SUCCESS.getValue());
                            return null;
                        })
                .when(transactionService)
                .updateEci(any());

        // When
        ARES result = authenticationRequestService.processAuthenticationRequest(validAReq);

        // Then
        assertNotNull(result);

        assertEquals(TransactionStatus.SUCCESS, transaction.getTransactionStatus());
        verify(transactionService, times(2)).updateEci(transaction);
        assertEquals("05", transaction.getEci());
        assertEquals(Phase.ARES, transaction.getPhase());
        assertEquals("MOCK_AUTH_VAL", transaction.getAuthValue());

        assertEquals("2.1.0", result.getMessageVersion());
        assertEquals("ARes", result.getMessageType());
        assertEquals("Y", result.getTransStatus());
        assertEquals("N", result.getAcsChallengeMandated());
        assertEquals("05", result.getEci());
        assertEquals(transaction.getId(), result.getAcsTransID());
        assertEquals("DEFAULT", result.getAcsOperatorID());
        assertEquals("referenceNumber", result.getAcsReferenceNumber());
        assertEquals("2c0f40ad-ca51-47ab-8da9-348b162673aa", result.getDsTransID());
        assertEquals("threeDSRequestorID_UTSB", result.getThreeDSServerTransID());
    }

    @Test
    void processAuthenticationRequest_SuccessfullyProcessesBRW_ChallengeFlow()
            throws ThreeDSException, ACSException { // Given
        AREQ validAReq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        when(transactionService.create(any())).thenReturn(transaction);
        doNothing().when(areqValidator).validateRequest(validAReq);
        when(cardRangeService.findByPan(any()))
                .thenReturn(TransactionTestData.createSampleCardRange());
        doNothing().when(cardRangeService).validateRange(any());
        doNothing().when(cardDetailService).validateAndUpdateCardDetails(any(), any(), any());
        doNothing().when(featureService).getACSRenderingType(any(), any());
        doNothing().when(challengeDetermineService).determineChallenge(any(), any(), any());
        when(transactionService.saveOrUpdate(any())).thenReturn(transaction);
        when(authValueGeneratorService.getAuthValue(any())).thenReturn("MOCK_AUTH_VAL");
        when(environment.getActiveProfiles()).thenReturn(new String[] {"test"});
        doNothing().when(transactionService).updateEci(any());
        AuthConfigDto authconfig =
                AuthConfigTestData.createAuthConfigDto(
                        OOBType.MOCK, true, true, AuthType.OTP, AuthType.OTP);
        when(featureService.getAuthenticationConfig((Transaction) any())).thenReturn(authconfig);

        doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            Transaction trans = (Transaction) args[1];
                            // Perform your updates on the transaction object
                            trans.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
                            trans.setChallengeMandated(true);
                            return null;
                        })
                .when(challengeDetermineService)
                .determineChallenge(any(), any(), any());

        TransactionTimerService scheduledTaskMock = mock(TransactionTimerService.class);
        when(transactionTimeoutServiceLocator.locateService(eq(MessageType.AReq)))
                .thenReturn(scheduledTaskMock);
        doNothing()
                .when(scheduledTaskMock)
                .scheduleTask(
                        eq(transaction.getId()), eq(transaction.getTransactionStatus()), any());

        // When
        ARES result = authenticationRequestService.processAuthenticationRequest(validAReq);

        // Then
        assertNotNull(result);

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, transaction.getTransactionStatus());
        verify(transactionService, times(1)).updateEci(transaction);
        assertNull(transaction.getEci());
        assertEquals(Phase.ARES, transaction.getPhase());
        assertNull(transaction.getAuthValue());

        assertEquals("2.1.0", result.getMessageVersion());
        assertEquals("ARes", result.getMessageType());
        assertEquals("C", result.getTransStatus());
        assertEquals("Y", result.getAcsChallengeMandated());
        assertEquals("www.test.com/v2/transaction/challenge/browser", result.getAcsURL());
        assertEquals("02", result.getAuthenticationType());
        assertEquals("N", result.getAcsDecConInd());
        assertEquals(transaction.getId(), result.getAcsTransID());
        assertEquals("DEFAULT", result.getAcsOperatorID());
        assertEquals("referenceNumber", result.getAcsReferenceNumber());
        assertEquals("2c0f40ad-ca51-47ab-8da9-348b162673aa", result.getDsTransID());
        assertEquals("threeDSRequestorID_UTSB", result.getThreeDSServerTransID());
    }

    // todo app based, exception and decoupled
    @Test
    void processAuthenticationRequest_ValidationFailedAreq() throws ThreeDSException, ACSException {
        // Given
        AREQ aReq = AREQTestData.createSampleAREQ();

        doThrow(
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid Message Version"))
                .when(areqValidator)
                .validateRequest(aReq);

        when(transactionService.saveOrUpdate(ArgumentMatchers.any()))
                .thenAnswer((Answer<Transaction>) invocation -> invocation.getArgument(0));

        // When
        ThreeDSException exception =
                assertThrows(
                        ThreeDSException.class,
                        () -> authenticationRequestService.processAuthenticationRequest(aReq));

        // Then
        assertNotNull(exception);
        assertEquals(
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, exception.getThreeDSecureErrorCode());

        Transaction transaction = exception.getTransaction();
        assertEquals(TransactionStatus.UNABLE_TO_AUTHENTICATE, transaction.getTransactionStatus());
        assertEquals(
                TransactionStatusReason.INVALID_TRANSACTION.getCode(),
                transaction.getTransactionStatusReason());
        assertEquals(Phase.AERROR, transaction.getPhase());
        assertEquals(InternalErrorCode.INVALID_REQUEST.getCode(), transaction.getErrorCode());
        assertNotNull(transaction.getId());
        //        assertEquals("2.1.0", transaction.getMessageVersion());
    }

    @Test
    void processAuthenticationRequest_card_range_DataNotFoundException()
            throws ThreeDSException, ACSException {
        // Given
        AREQ aReq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        when(transactionService.create(any())).thenReturn(transaction);
        doNothing().when(areqValidator).validateRequest(aReq);
        when(cardRangeService.findByPan(any()))
                .thenReturn(TransactionTestData.createSampleCardRange());
        doThrow(
                        new DataNotFoundException(
                                ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                                InternalErrorCode.CARD_RANGE_NOT_FOUND))
                .when(cardRangeService)
                .validateRange(any());

        when(transactionService.saveOrUpdate(ArgumentMatchers.any()))
                .thenAnswer((Answer<Transaction>) invocation -> invocation.getArgument(0));

        // When
        ThreeDSException exception =
                assertThrows(
                        ThreeDSException.class,
                        () -> authenticationRequestService.processAuthenticationRequest(aReq));

        // Then
        assertNotNull(exception);
        assertEquals(
                ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                exception.getThreeDSecureErrorCode());

        transaction = exception.getTransaction();
        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(
                TransactionStatusReason.NO_CARD_RECORD.getCode(),
                transaction.getTransactionStatusReason());
        assertEquals(Phase.AERROR, transaction.getPhase());
        assertEquals(InternalErrorCode.CARD_RANGE_NOT_FOUND.getCode(), transaction.getErrorCode());
        assertNotNull(transaction.getId());
        assertEquals("2.2.0", transaction.getMessageVersion());
    }

    @Test
    void processAuthenticationRequest_card_range_CardDetailsNotFoundException()
            throws ThreeDSException, ACSException {
        // Given
        AREQ aReq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        ARES aRes = AREQTestData.createSampleARES();
        when(transactionService.create(any())).thenReturn(transaction);
        doNothing().when(areqValidator).validateRequest(aReq);
        when(cardRangeService.findByPan(any()))
                .thenReturn(TransactionTestData.createSampleCardRange());
        doNothing().when(cardRangeService).validateRange(any());
        doThrow(
                        new CardDetailsNotFoundException(
                                InternalErrorCode.CARD_USER_FETCH_EXCEPTION,
                                "error during fetching user data"))
                .when(cardDetailService)
                .validateAndUpdateCardDetails(any(), any(), any());

        when(transactionService.saveOrUpdate(ArgumentMatchers.any()))
                .thenAnswer((Answer<Transaction>) invocation -> invocation.getArgument(0));
        // When
        ARES ares = authenticationRequestService.processAuthenticationRequest(aReq);

        // Then
        verify(transactionService, times(1)).updateEci(transaction);
        assertNotNull(ares);

        assertEquals("2.1.0", ares.getMessageVersion());
        assertEquals("ARes", ares.getMessageType());
        assertEquals("U", ares.getTransStatus());
        assertEquals(TransactionStatusReason.NO_CARD_RECORD.getCode(), ares.getTransStatusReason());
        assertEquals("N", ares.getAcsChallengeMandated());
        assertEquals("www.test.com/v2/transaction/challenge/browser", ares.getAcsURL());
        assertEquals("02", ares.getAuthenticationType());
        assertEquals("N", ares.getAcsDecConInd());
        assertEquals(transaction.getId(), ares.getAcsTransID());
        assertEquals("DEFAULT", ares.getAcsOperatorID());
        assertEquals("referenceNumber", ares.getAcsReferenceNumber());
        assertEquals("2c0f40ad-ca51-47ab-8da9-348b162673aa", ares.getDsTransID());
        assertEquals("threeDSRequestorID_UTSB", ares.getThreeDSServerTransID());
    }
}
