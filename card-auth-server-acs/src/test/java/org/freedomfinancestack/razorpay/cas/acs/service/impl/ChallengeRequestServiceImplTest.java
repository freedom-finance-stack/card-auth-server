package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Map;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.data.*;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.*;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.ChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.ChallengeRequestParserFactory;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.impl.AppChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.impl.BrowserChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.CReqTransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.locator.TransactionTimeoutServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChallengeRequestServiceImplTest {
    // TODO: add more test cases and merge some test cases together
    private static final String SAMPLE_THREEDS_SESSION_DATA = "dGhyZWVEc1Nlc3Npb25EYXRh";

    @Mock private TransactionService transactionService;
    @Mock private TransactionMessageLogService transactionMessageLogService;
    @Mock private ChallengeRequestValidator challengeRequestValidator;
    @Mock private FeatureService featureService;
    @Mock private AuthenticationServiceLocator authenticationServiceLocator;
    @Mock private ResultRequestService resultRequestService;
    @Mock private CardDetailService cardDetailService;
    @Mock private CardRangeService cardRangeService;
    @Mock private CResMapper cResMapper;
    @Mock private AuthValueGeneratorService authValueGeneratorService;
    @Mock private TransactionTimeoutServiceLocator transactionTimeoutServiceLocator;
    @Mock private ChallengeRequestParserFactory challengeRequestParserFactory;
    @Mock private DecoupledAuthenticationService decoupledAuthenticationService;
    @Mock private InstitutionUiService institutionUiService;

    @InjectMocks private ChallengeRequestServiceImpl challengeRequestService;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void processChallengeRequest_Success_Send(String deviceChannelStr) throws Exception {

        DeviceChannel deviceChannel = DeviceChannel.getDeviceChannel(deviceChannelStr);
        CREQ creq;
        Transaction transaction = TransactionTestData.createSampleTransaction(deviceChannelStr);
        transaction.setPhase(Phase.ARES);
        transaction.setChallengeMandated(true);

        if (deviceChannel.equals(DeviceChannel.APP)) {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                    "sdkCounterStoA", InternalConstants.INITIAL_ACS_SDK_COUNTER));
            transaction
                    .getTransactionSdkDetail()
                    .setAcsCounterAtoS(InternalConstants.INITIAL_ACS_SDK_COUNTER);
        } else {
            creq = CREQTestData.createCREQ(Map.of("challengeWindowSize", "02"));
        }

        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ChallengeRequestParser challengeRequestParser =
                deviceChannel.equals(DeviceChannel.APP)
                        ? mock(AppChallengeRequestParser.class)
                        : mock(BrowserChallengeRequestParser.class);
        when(challengeRequestParserFactory.getService(eq(deviceChannel)))
                .thenReturn(challengeRequestParser);
        when(challengeRequestParser.parseEncryptedRequest(any())).thenReturn(creq);

        when(transactionService.findById(eq(creq.getAcsTransID()))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CREQ.class), anyString());

        doNothing()
                .when(challengeRequestValidator)
                .validateRequest(any(CREQ.class), any(Transaction.class));

        when(featureService.getAuthenticationConfig(any(Transaction.class)))
                .thenReturn(authConfigDto);

        when(challengeRequestValidator.isWhitelistingDataValid(
                        any(Transaction.class), any(CREQ.class), any(AuthConfigDto.class)))
                .thenReturn(true);

        TransactionTimerService transactionTimerService = mock(CReqTransactionTimerService.class);
        when(transactionTimeoutServiceLocator.locateService(any()))
                .thenReturn(transactionTimerService);
        doNothing().when(transactionTimerService).scheduleTask(anyString(), any(), any());

        AuthenticationService authenticationService = mock(OTPAuthenticationService.class);
        when(authenticationServiceLocator.locateTransactionAuthenticationService(
                        eq(transaction), any()))
                .thenReturn(authenticationService);
        doNothing().when(authenticationService).preAuthenticate(any());

        ArgumentCaptor<ChallengeFlowDto> challengeFlowDtoArgumentCaptor =
                ArgumentCaptor.forClass(ChallengeFlowDto.class);
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);
        doAnswer(
                        invocation -> {
                            ChallengeFlowDto capturedChallengeFlowDto =
                                    challengeFlowDtoArgumentCaptor.getValue();
                            capturedChallengeFlowDto.setInstitutionUIParams(institutionUIParams);

                            return null;
                        })
                .when(institutionUiService)
                .populateUiParams(
                        challengeFlowDtoArgumentCaptor.capture(), any(AuthConfigDto.class));

        CRES cres = CRESTestData.createCRES(transaction, institutionUIParams);
        when(cResMapper.toCRes(any(Transaction.class), any(InstitutionUIParams.class)))
                .thenReturn(cres);

        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        doAnswer(
                        invocation -> {
                            Transaction capturedTransaction = transactionArgumentCaptor.getValue();
                            capturedTransaction.setEci("test_eci");

                            return null;
                        })
                .when(transactionService)
                .updateEci(transactionArgumentCaptor.capture());

        when(transactionService.saveOrUpdate(any(Transaction.class))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CRES.class), anyString());

        if (deviceChannel.equals(DeviceChannel.APP)) {
            String cresStr = cres.toString();
            when(challengeRequestParser.generateEncryptedResponse(
                            any(ChallengeFlowDto.class), any(Transaction.class)))
                    .thenReturn(cresStr);
        }

        ChallengeFlowDto challengeFlowDto =
                challengeRequestService.processChallengeRequest(
                        deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA);

        assertNotNull(challengeFlowDto);
        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertEquals(
                deviceChannelStr, challengeFlowDto.getInstitutionUIParams().getDeviceChannel());
        assertNull(challengeFlowDto.getInstitutionUIParams().getOobContinueLabel());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertEquals("100.00", challengeFlowDto.getInstitutionUIParams().getAmount());
        assertEquals("USD", challengeFlowDto.getInstitutionUIParams().getCurrency());
        if (deviceChannel.equals(DeviceChannel.APP)) {
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            } else {
                assertEquals(
                        UiParamsTestData.SAMPLE_ENCODED_DISPLAY_PAGE,
                        challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            }
        } else if (deviceChannel.equals(DeviceChannel.BRW)) {
            assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
        }
        assertEquals(Phase.CDRES, transaction.getPhase());
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void processChallengeRequest_Success_ReSend(String deviceChannelStr) throws Exception {

        DeviceChannel deviceChannel = DeviceChannel.getDeviceChannel(deviceChannelStr);
        CREQ creq;
        Transaction transaction = TransactionTestData.createSampleTransaction(deviceChannelStr);
        transaction.setPhase(Phase.CDRES);
        transaction.setChallengeMandated(true);

        if (deviceChannel.equals(DeviceChannel.APP)) {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                    "sdkCounterStoA", "001",
                                    "resendChallenge", InternalConstants.YES));
        } else {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "challengeWindowSize",
                                    "02",
                                    "resendChallenge",
                                    InternalConstants.YES));
        }

        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ChallengeRequestParser challengeRequestParser =
                deviceChannel.equals(DeviceChannel.APP)
                        ? mock(AppChallengeRequestParser.class)
                        : mock(BrowserChallengeRequestParser.class);
        when(challengeRequestParserFactory.getService(eq(deviceChannel)))
                .thenReturn(challengeRequestParser);
        when(challengeRequestParser.parseEncryptedRequest(any())).thenReturn(creq);

        when(transactionService.findById(eq(creq.getAcsTransID()))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CREQ.class), anyString());

        doNothing()
                .when(challengeRequestValidator)
                .validateRequest(any(CREQ.class), any(Transaction.class));

        when(featureService.getAuthenticationConfig(any(Transaction.class)))
                .thenReturn(authConfigDto);

        when(challengeRequestValidator.isWhitelistingDataValid(
                        any(Transaction.class), any(CREQ.class), any(AuthConfigDto.class)))
                .thenReturn(true);

        AuthenticationService authenticationService = mock(OTPAuthenticationService.class);
        when(authenticationServiceLocator.locateTransactionAuthenticationService(
                        eq(transaction), any()))
                .thenReturn(authenticationService);
        doNothing().when(authenticationService).preAuthenticate(any());

        ArgumentCaptor<ChallengeFlowDto> challengeFlowDtoArgumentCaptor =
                ArgumentCaptor.forClass(ChallengeFlowDto.class);
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);
        doAnswer(
                        invocation -> {
                            ChallengeFlowDto capturedChallengeFlowDto =
                                    challengeFlowDtoArgumentCaptor.getValue();
                            capturedChallengeFlowDto.setInstitutionUIParams(institutionUIParams);

                            return null;
                        })
                .when(institutionUiService)
                .populateUiParams(
                        challengeFlowDtoArgumentCaptor.capture(), any(AuthConfigDto.class));

        CRES cres = CRESTestData.createCRES(transaction, institutionUIParams);
        when(cResMapper.toCRes(any(Transaction.class), any(InstitutionUIParams.class)))
                .thenReturn(cres);

        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        doAnswer(
                        invocation -> {
                            Transaction capturedTransaction = transactionArgumentCaptor.getValue();
                            capturedTransaction.setEci("test_eci");

                            return null;
                        })
                .when(transactionService)
                .updateEci(transactionArgumentCaptor.capture());

        when(transactionService.saveOrUpdate(any(Transaction.class))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CRES.class), anyString());

        if (deviceChannel.equals(DeviceChannel.APP)) {
            String cresStr = cres.toString();
            when(challengeRequestParser.generateEncryptedResponse(
                            any(ChallengeFlowDto.class), any(Transaction.class)))
                    .thenReturn(cresStr);
        }

        ChallengeFlowDto challengeFlowDto =
                challengeRequestService.processChallengeRequest(
                        deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA);

        assertNotNull(challengeFlowDto);
        assertEquals(InternalConstants.RESEND, challengeFlowDto.getCurrentFlowType());
        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertEquals(
                deviceChannelStr, challengeFlowDto.getInstitutionUIParams().getDeviceChannel());
        assertNull(challengeFlowDto.getInstitutionUIParams().getOobContinueLabel());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertEquals("100.00", challengeFlowDto.getInstitutionUIParams().getAmount());
        assertEquals("USD", challengeFlowDto.getInstitutionUIParams().getCurrency());
        assertEquals(
                "sampleResendInformationLabel",
                challengeFlowDto.getInstitutionUIParams().getResendInformationLabel());
        if (deviceChannel.equals(DeviceChannel.APP)) {
            assertEquals("001", challengeFlowDto.getCres().getAcsCounterAtoS());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            } else {
                assertEquals(
                        UiParamsTestData.SAMPLE_ENCODED_DISPLAY_PAGE,
                        challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            }
        } else if (deviceChannel.equals(DeviceChannel.BRW)) {
            assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
        }
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void processChallengeRequest_Success_Cancel(String deviceChannelStr) throws Exception {

        DeviceChannel deviceChannel = DeviceChannel.getDeviceChannel(deviceChannelStr);
        CREQ creq;
        Transaction transaction = TransactionTestData.createSampleTransaction(deviceChannelStr);
        transaction.setPhase(Phase.CDRES);
        transaction.setMessageCategory(MessageCategory.NPA);
        transaction.setChallengeMandated(true);

        if (deviceChannel.equals(DeviceChannel.APP)) {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                    "sdkCounterStoA", "001",
                                    "challengeCancel", "01"));
        } else {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "challengeWindowSize", "02",
                                    "challengeCancel", "01"));
        }

        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ChallengeRequestParser challengeRequestParser =
                deviceChannel.equals(DeviceChannel.APP)
                        ? mock(AppChallengeRequestParser.class)
                        : mock(BrowserChallengeRequestParser.class);
        when(challengeRequestParserFactory.getService(eq(deviceChannel)))
                .thenReturn(challengeRequestParser);
        when(challengeRequestParser.parseEncryptedRequest(any())).thenReturn(creq);

        when(transactionService.findById(eq(creq.getAcsTransID()))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CREQ.class), anyString());

        doNothing()
                .when(challengeRequestValidator)
                .validateRequest(any(CREQ.class), any(Transaction.class));

        when(featureService.getAuthenticationConfig(any(Transaction.class)))
                .thenReturn(authConfigDto);

        when(challengeRequestValidator.isWhitelistingDataValid(
                        any(Transaction.class), any(CREQ.class), any(AuthConfigDto.class)))
                .thenReturn(true);

        ArgumentCaptor<ChallengeFlowDto> challengeFlowDtoArgumentCaptor =
                ArgumentCaptor.forClass(ChallengeFlowDto.class);
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);
        doAnswer(
                        invocation -> {
                            ChallengeFlowDto capturedChallengeFlowDto =
                                    challengeFlowDtoArgumentCaptor.getValue();
                            capturedChallengeFlowDto.setInstitutionUIParams(institutionUIParams);

                            return null;
                        })
                .when(institutionUiService)
                .populateUiParams(
                        challengeFlowDtoArgumentCaptor.capture(), any(AuthConfigDto.class));

        CRES cres = CRESTestData.createFinalCRes(transaction);
        when(cResMapper.toFinalCRes(any(Transaction.class))).thenReturn(cres);

        TransactionTimerService transactionTimerService = mock(CReqTransactionTimerService.class);
        when(transactionTimeoutServiceLocator.locateService(any()))
                .thenReturn(transactionTimerService);
        doNothing().when(transactionTimerService).cancelTask(anyString());

        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        doAnswer(
                        invocation -> {
                            Transaction capturedTransaction = transactionArgumentCaptor.getValue();
                            capturedTransaction.setEci("test_eci");

                            return null;
                        })
                .when(transactionService)
                .updateEci(transactionArgumentCaptor.capture());

        doNothing().when(resultRequestService).handleRreq(any(Transaction.class));

        when(transactionService.saveOrUpdate(any(Transaction.class))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CRES.class), anyString());

        if (deviceChannel.equals(DeviceChannel.APP)) {
            String cresStr = cres.toString();
            when(challengeRequestParser.generateEncryptedResponse(
                            any(ChallengeFlowDto.class), any(Transaction.class)))
                    .thenReturn(cresStr);
        }

        ChallengeFlowDto challengeFlowDto =
                challengeRequestService.processChallengeRequest(
                        deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA);

        assertNotNull(challengeFlowDto);
        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertEquals(
                deviceChannelStr, challengeFlowDto.getInstitutionUIParams().getDeviceChannel());
        assertNull(challengeFlowDto.getInstitutionUIParams().getOobContinueLabel());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertNull(challengeFlowDto.getInstitutionUIParams().getAmount());
        assertNull(challengeFlowDto.getInstitutionUIParams().getCurrency());
        assertEquals(
                "sampleResendInformationLabel",
                challengeFlowDto.getInstitutionUIParams().getResendInformationLabel());
        if (deviceChannel.equals(DeviceChannel.APP)) {
            assertEquals("001", challengeFlowDto.getCres().getAcsCounterAtoS());
            assertEquals(
                    InternalConstants.NO, challengeFlowDto.getCres().getChallengeCompletionInd());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            } else {
                assertEquals(
                        UiParamsTestData.SAMPLE_ENCODED_DISPLAY_PAGE,
                        challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            }
        } else if (deviceChannel.equals(DeviceChannel.BRW)) {
            assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
        }
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void processChallengeRequest_Success_Otp_Validation_Incorrect(String deviceChannelStr)
            throws Exception {

        DeviceChannel deviceChannel = DeviceChannel.getDeviceChannel(deviceChannelStr);
        CREQ creq;
        Transaction transaction = TransactionTestData.createSampleTransaction(deviceChannelStr);
        transaction.setPhase(Phase.CDRES);
        transaction.setChallengeMandated(true);

        if (deviceChannel.equals(DeviceChannel.APP)) {
            transaction.getTransactionSdkDetail().setAcsInterface(DeviceInterface.HTML.getValue());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                creq =
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkTransID",
                                        TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "sdkCounterStoA",
                                        "001",
                                        "challengeDataEntry",
                                        "0000",
                                        "whitelistingDataEntry",
                                        InternalConstants.YES));
            } else {
                creq =
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "sdkCounterStoA", "001",
                                        "challengeHTMLDataEntry", "0000"));
            }
        } else {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "challengeWindowSize", "02",
                                    "challengeHTMLDataEntry", "0000"));
        }

        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ChallengeRequestParser challengeRequestParser =
                deviceChannel.equals(DeviceChannel.APP)
                        ? mock(AppChallengeRequestParser.class)
                        : mock(BrowserChallengeRequestParser.class);
        when(challengeRequestParserFactory.getService(eq(deviceChannel)))
                .thenReturn(challengeRequestParser);
        when(challengeRequestParser.parseEncryptedRequest(any())).thenReturn(creq);

        when(transactionService.findById(eq(creq.getAcsTransID()))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CREQ.class), anyString());

        doNothing()
                .when(challengeRequestValidator)
                .validateRequest(any(CREQ.class), any(Transaction.class));

        when(featureService.getAuthenticationConfig(any(Transaction.class)))
                .thenReturn(authConfigDto);

        when(challengeRequestValidator.isWhitelistingDataValid(
                        any(Transaction.class), any(CREQ.class), any(AuthConfigDto.class)))
                .thenReturn(true);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(false);
        authResponse.setDisplayMessage(InternalConstants.CHALLENGE_INCORRECT_OTP_TEXT);
        AuthenticationService authenticationService = mock(OTPAuthenticationService.class);
        when(authenticationServiceLocator.locateTransactionAuthenticationService(
                        eq(transaction), any()))
                .thenReturn(authenticationService);
        when(authenticationService.authenticate(any(AuthenticationDto.class)))
                .thenReturn(authResponse);

        ArgumentCaptor<ChallengeFlowDto> challengeFlowDtoArgumentCaptor =
                ArgumentCaptor.forClass(ChallengeFlowDto.class);
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);
        doAnswer(
                        invocation -> {
                            ChallengeFlowDto capturedChallengeFlowDto =
                                    challengeFlowDtoArgumentCaptor.getValue();
                            capturedChallengeFlowDto.setInstitutionUIParams(institutionUIParams);

                            return null;
                        })
                .when(institutionUiService)
                .populateUiParams(
                        challengeFlowDtoArgumentCaptor.capture(), any(AuthConfigDto.class));

        CRES cres = CRESTestData.createCRES(transaction, institutionUIParams);
        when(cResMapper.toCRes(any(Transaction.class), any(InstitutionUIParams.class)))
                .thenReturn(cres);

        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        doAnswer(
                        invocation -> {
                            Transaction capturedTransaction = transactionArgumentCaptor.getValue();
                            capturedTransaction.setEci("test_eci");

                            return null;
                        })
                .when(transactionService)
                .updateEci(transactionArgumentCaptor.capture());

        when(transactionService.saveOrUpdate(any(Transaction.class))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CRES.class), anyString());

        if (deviceChannel.equals(DeviceChannel.APP)) {
            String cresStr = cres.toString();
            when(challengeRequestParser.generateEncryptedResponse(
                            any(ChallengeFlowDto.class), any(Transaction.class)))
                    .thenReturn(cresStr);
        }

        ChallengeFlowDto challengeFlowDto =
                challengeRequestService.processChallengeRequest(
                        deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA);

        assertNotNull(challengeFlowDto);
        assertEquals(InternalConstants.VALIDATE_OTP, challengeFlowDto.getCurrentFlowType());
        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertEquals(
                deviceChannelStr, challengeFlowDto.getInstitutionUIParams().getDeviceChannel());
        assertNull(challengeFlowDto.getInstitutionUIParams().getOobContinueLabel());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertEquals(
                "sampleResendInformationLabel",
                challengeFlowDto.getInstitutionUIParams().getResendInformationLabel());
        if (deviceChannel.equals(DeviceChannel.APP)) {
            assertEquals("001", challengeFlowDto.getCres().getAcsCounterAtoS());
            assertEquals(
                    InternalConstants.NO, challengeFlowDto.getCres().getChallengeCompletionInd());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            } else {
                assertEquals(
                        UiParamsTestData.SAMPLE_ENCODED_DISPLAY_PAGE,
                        challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            }
        } else if (deviceChannel.equals(DeviceChannel.BRW)) {
            assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
        }
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void processChallengeRequest_Success_Otp_Validation_Correct(String deviceChannelStr)
            throws Exception {

        DeviceChannel deviceChannel = DeviceChannel.getDeviceChannel(deviceChannelStr);
        CREQ creq;
        Transaction transaction = TransactionTestData.createSampleTransaction(deviceChannelStr);
        transaction.setPhase(Phase.CDRES);
        transaction.setChallengeMandated(true);

        if (deviceChannel.equals(DeviceChannel.APP)) {
            transaction.getTransactionSdkDetail().setAcsInterface(DeviceInterface.HTML.getValue());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                creq =
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkTransID",
                                        TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "sdkCounterStoA",
                                        "001",
                                        "challengeDataEntry",
                                        "0000",
                                        "whitelistingDataEntry",
                                        InternalConstants.YES));
            } else {
                creq =
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "sdkCounterStoA", "001",
                                        "challengeHTMLDataEntry", "0000"));
            }
        } else {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "challengeWindowSize", "02",
                                    "challengeHTMLDataEntry", "0000"));
        }

        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ChallengeRequestParser challengeRequestParser =
                deviceChannel.equals(DeviceChannel.APP)
                        ? mock(AppChallengeRequestParser.class)
                        : mock(BrowserChallengeRequestParser.class);
        when(challengeRequestParserFactory.getService(eq(deviceChannel)))
                .thenReturn(challengeRequestParser);
        when(challengeRequestParser.parseEncryptedRequest(any())).thenReturn(creq);

        when(transactionService.findById(eq(creq.getAcsTransID()))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CREQ.class), anyString());

        doNothing()
                .when(challengeRequestValidator)
                .validateRequest(any(CREQ.class), any(Transaction.class));

        when(featureService.getAuthenticationConfig(any(Transaction.class)))
                .thenReturn(authConfigDto);

        when(challengeRequestValidator.isWhitelistingDataValid(
                        any(Transaction.class), any(CREQ.class), any(AuthConfigDto.class)))
                .thenReturn(true);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(true);
        AuthenticationService authenticationService = mock(OTPAuthenticationService.class);
        when(authenticationServiceLocator.locateTransactionAuthenticationService(
                        eq(transaction), any()))
                .thenReturn(authenticationService);
        when(authenticationService.authenticate(any(AuthenticationDto.class)))
                .thenReturn(authResponse);

        when(authValueGeneratorService.getAuthValue(any(Transaction.class))).thenReturn("0000");

        ArgumentCaptor<ChallengeFlowDto> challengeFlowDtoArgumentCaptor =
                ArgumentCaptor.forClass(ChallengeFlowDto.class);
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);
        doAnswer(
                        invocation -> {
                            ChallengeFlowDto capturedChallengeFlowDto =
                                    challengeFlowDtoArgumentCaptor.getValue();
                            capturedChallengeFlowDto.setInstitutionUIParams(institutionUIParams);

                            return null;
                        })
                .when(institutionUiService)
                .populateUiParams(
                        challengeFlowDtoArgumentCaptor.capture(), any(AuthConfigDto.class));

        CRES cres = CRESTestData.createFinalCRes(transaction);
        when(cResMapper.toFinalCRes(any(Transaction.class))).thenReturn(cres);

        TransactionTimerService transactionTimerService = mock(CReqTransactionTimerService.class);
        when(transactionTimeoutServiceLocator.locateService(any()))
                .thenReturn(transactionTimerService);
        doNothing().when(transactionTimerService).cancelTask(anyString());

        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        doAnswer(
                        invocation -> {
                            Transaction capturedTransaction = transactionArgumentCaptor.getValue();
                            capturedTransaction.setEci("test_eci");

                            return null;
                        })
                .when(transactionService)
                .updateEci(transactionArgumentCaptor.capture());

        doNothing().when(resultRequestService).handleRreq(any(Transaction.class));

        when(transactionService.saveOrUpdate(any(Transaction.class))).thenReturn(transaction);

        doNothing().when(transactionMessageLogService).createAndSave(any(CRES.class), anyString());

        if (deviceChannel.equals(DeviceChannel.APP)) {
            String cresStr = cres.toString();
            when(challengeRequestParser.generateEncryptedResponse(
                            any(ChallengeFlowDto.class), any(Transaction.class)))
                    .thenReturn(cresStr);
        }

        ChallengeFlowDto challengeFlowDto =
                challengeRequestService.processChallengeRequest(
                        deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA);

        assertNotNull(challengeFlowDto);
        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertEquals(
                deviceChannelStr, challengeFlowDto.getInstitutionUIParams().getDeviceChannel());
        assertNull(challengeFlowDto.getInstitutionUIParams().getOobContinueLabel());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertEquals(
                "sampleResendInformationLabel",
                challengeFlowDto.getInstitutionUIParams().getResendInformationLabel());
        if (deviceChannel.equals(DeviceChannel.APP)) {
            assertEquals("001", challengeFlowDto.getCres().getAcsCounterAtoS());
            assertEquals(
                    InternalConstants.NO, challengeFlowDto.getCres().getChallengeCompletionInd());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            } else {
                assertEquals(
                        UiParamsTestData.SAMPLE_ENCODED_DISPLAY_PAGE,
                        challengeFlowDto.getInstitutionUIParams().getDisplayPage());
            }
        } else if (deviceChannel.equals(DeviceChannel.BRW)) {
            assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
        }
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void processChallengeRequest_Failure_Challenge_Not_Mandated(String deviceChannelStr)
            throws Exception {
        DeviceChannel deviceChannel = DeviceChannel.getDeviceChannel(deviceChannelStr);
        CREQ creq;
        Transaction transaction = TransactionTestData.createSampleTransaction(deviceChannelStr);
        transaction.setPhase(Phase.ARES);

        if (deviceChannel.equals(DeviceChannel.APP)) {
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                creq =
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkTransID",
                                        TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "sdkCounterStoA",
                                        "001",
                                        "challengeDataEntry",
                                        "0000",
                                        "whitelistingDataEntry",
                                        InternalConstants.YES));
            } else {
                creq =
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "sdkCounterStoA", "001",
                                        "challengeHTMLDataEntry", "0000"));
            }
        } else {
            creq =
                    CREQTestData.createCREQ(
                            Map.of(
                                    "challengeWindowSize", "02",
                                    "challengeHTMLDataEntry", "0000"));
        }

        when(transactionService.findById(eq(creq.getAcsTransID()))).thenReturn(transaction);

        ChallengeRequestParser challengeRequestParser =
                deviceChannel.equals(DeviceChannel.APP)
                        ? mock(AppChallengeRequestParser.class)
                        : mock(BrowserChallengeRequestParser.class);
        when(challengeRequestParserFactory.getService(eq(deviceChannel)))
                .thenReturn(challengeRequestParser);
        when(challengeRequestParser.parseEncryptedRequest(any())).thenReturn(creq);

        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        doAnswer(
                        invocation -> {
                            Transaction capturedTransaction = transactionArgumentCaptor.getValue();
                            capturedTransaction.setEci("test_eci");

                            return null;
                        })
                .when(transactionService)
                .updateEci(transactionArgumentCaptor.capture());

        if (deviceChannel.equals(DeviceChannel.APP)) {
            ThreeDSException exception =
                    assertThrows(
                            ThreeDSException.class,
                            () ->
                                    challengeRequestService.processChallengeRequest(
                                            deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA));

            assertEquals("6002 : INVALID REQUEST", exception.getMessage());
            assertNull(exception.getInternalErrorCode());
            assertEquals(
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    exception.getThreeDSecureErrorCode());
        } else {
            ChallengeFlowDto challengeFlowDto =
                    challengeRequestService.processChallengeRequest(
                            deviceChannel, "strCReq", SAMPLE_THREEDS_SESSION_DATA);

            assertNotNull(challengeFlowDto);
            assertEquals(
                    HttpStatus.OK.value(), challengeFlowDto.getErrorResponse().getHttpStatus());
            assertEquals(
                    MessageType.Erro.toString(),
                    challengeFlowDto.getErrorResponse().getMessageType());
            assertEquals("2.2.0", challengeFlowDto.getErrorResponse().getMessageVersion());
            assertEquals(
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID.getErrorCode(),
                    challengeFlowDto.getErrorResponse().getErrorCode());
        }
    }
}
