package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.UiParamsTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ImageProcessingException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSRequestorChallengeInd;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstitutionUiParamsMapperTest {

    @Mock private AppConfiguration appConfiguration;

    @Mock private InstitutionUiConfiguration institutionUiConfiguration;

    @Mock private TestConfigProperties testConfigProperties;

    @InjectMocks private InstitutionUiParamsMapperImpl institutionUiParamsMapper;

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void getAmount(String messageCategory) {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setMessageCategory(MessageCategory.getMessageCategory(messageCategory));
        String amount = institutionUiParamsMapper.getAmount(transaction);

        if (messageCategory.equals(MessageCategory.PA.getCategory())) {
            assertNotNull(amount);
            assertEquals("100.00", amount);
        } else {
            assertNull(amount);
        }
    }

    @ParameterizedTest
    @CsvSource({"01", "02"})
    void getCurrency(String messageCategory) {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setMessageCategory(MessageCategory.getMessageCategory(messageCategory));
        String currency = institutionUiParamsMapper.getCurrency(transaction);

        if (messageCategory.equals(MessageCategory.PA.getCategory())) {
            assertNotNull(currency);
            assertEquals("USD", currency);
        } else {
            assertNull(currency);
        }
    }

    @Test
    void getValidationUrl_Success_1() {
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        when(appConfiguration.getHostname()).thenReturn("http://localhost:8080");

        String validationUrl =
                institutionUiParamsMapper.getValidationUrl(
                        transaction, institutionUiConfiguration, appConfiguration);

        assertNotNull(validationUrl);
        assertEquals(
                "http://localhost:8080/v2/transaction/challenge/browser/validate", validationUrl);
    }

    @Test
    void getValidationUrl_Success_2() {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionSdkDetail().setAcsInterface("02");

        when(institutionUiConfiguration.getInstitutionCssUrl()).thenReturn("http://localhost:8080");

        String validationUrl =
                institutionUiParamsMapper.getValidationUrl(
                        transaction, institutionUiConfiguration, appConfiguration);

        assertNotNull(validationUrl);
        assertEquals("http://localhost:8080", validationUrl);
    }

    @Test
    void getIssuerImage_Success_Url() throws ImageProcessingException {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        String logoUrl = "https://drive.google.com/uc?id=1lYxWs3uk_PpV7TAL2MGEwQ1uFAEaxqLM";
        when(institutionUiConfiguration.getMediumLogo()).thenReturn(logoUrl);
        when(institutionUiConfiguration.getHighLogo()).thenReturn(logoUrl);
        when(institutionUiConfiguration.getExtraHighLogo()).thenReturn(logoUrl);

        Image issuerImage =
                institutionUiParamsMapper.getIssuerImage(transaction, institutionUiConfiguration);

        assertNotNull(issuerImage);
        assertEquals(logoUrl, issuerImage.getMedium());
        assertEquals(logoUrl, issuerImage.getHigh());
        assertEquals(logoUrl, issuerImage.getExtraHigh());
    }

    @Test
    void getIssuerImage_Success_Image() throws ImageProcessingException {
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        String logoUrl = "https://drive.google.com/uc?id=1lYxWs3uk_PpV7TAL2MGEwQ1uFAEaxqLM";
        when(institutionUiConfiguration.getMediumLogo()).thenReturn(logoUrl);
        when(institutionUiConfiguration.getHighLogo()).thenReturn(logoUrl);
        when(institutionUiConfiguration.getExtraHighLogo()).thenReturn(logoUrl);

        Image issuerImage =
                institutionUiParamsMapper.getIssuerImage(transaction, institutionUiConfiguration);

        assertNotNull(issuerImage);
        assertEquals(Util.getBase64Image(logoUrl), issuerImage.getMedium());
        assertEquals(Util.getBase64Image(logoUrl), issuerImage.getHigh());
        assertEquals(Util.getBase64Image(logoUrl), issuerImage.getExtraHigh());
    }

    @Test
    void getPsImage_Success_Url() throws ImageProcessingException {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());

        String logoUrl = "https://drive.google.com/uc?id=1jgUdDudqRTjB8Q36u0vMBakxpGWAwIdO";

        InstitutionUiConfiguration.UiConfig uiConfig = new InstitutionUiConfiguration.UiConfig();
        uiConfig.setMediumPs(logoUrl);
        uiConfig.setHighPs(logoUrl);
        uiConfig.setExtraHighPs(logoUrl);
        Map<Network, InstitutionUiConfiguration.UiConfig> networkUiConfigMap =
                new EnumMap<>(Network.class);
        networkUiConfigMap.put(network, uiConfig);
        when(institutionUiConfiguration.getNetworkUiConfig()).thenReturn(networkUiConfigMap);

        Image psImage =
                institutionUiParamsMapper.getPsImage(transaction, institutionUiConfiguration);

        assertNotNull(psImage);
        assertEquals(logoUrl, psImage.getMedium());
        assertEquals(logoUrl, psImage.getHigh());
        assertEquals(logoUrl, psImage.getExtraHigh());
    }

    @Test
    void getPsImage_Success_Image() throws ImageProcessingException {
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();

        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());

        String logoUrl = "https://drive.google.com/uc?id=1jgUdDudqRTjB8Q36u0vMBakxpGWAwIdO";

        InstitutionUiConfiguration.UiConfig uiConfig = new InstitutionUiConfiguration.UiConfig();
        uiConfig.setMediumPs(logoUrl);
        uiConfig.setHighPs(logoUrl);
        uiConfig.setExtraHighPs(logoUrl);
        Map<Network, InstitutionUiConfiguration.UiConfig> networkUiConfigMap =
                new EnumMap<>(Network.class);
        networkUiConfigMap.put(network, uiConfig);
        when(institutionUiConfiguration.getNetworkUiConfig()).thenReturn(networkUiConfigMap);

        Image psImage =
                institutionUiParamsMapper.getPsImage(transaction, institutionUiConfiguration);

        assertNotNull(psImage);
        assertEquals(Util.getBase64Image(logoUrl), psImage.getMedium());
        assertEquals(Util.getBase64Image(logoUrl), psImage.getHigh());
        assertEquals(Util.getBase64Image(logoUrl), psImage.getExtraHigh());
    }

    @Test
    void getWhitelistingInfoText_Success_Non_Null() {
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction
                .getTransactionReferenceDetail()
                .setThreeDSRequestorChallengeInd(
                        ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .getValue());
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();

        String whitelistingInfoText =
                institutionUiParamsMapper.getWhitelistingInfoText(
                        transaction, authConfigDto, institutionUiConfig);

        assertNotNull(whitelistingInfoText);
        assertEquals("sampleWhitelistingInfoText", whitelistingInfoText);
    }

    @Test
    void getWhitelistingInfoText_Success_Null() {
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction
                .getTransactionReferenceDetail()
                .setThreeDSRequestorChallengeInd(
                        ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .getValue());
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();

        String whitelistingInfoText =
                institutionUiParamsMapper.getWhitelistingInfoText(
                        transaction, authConfigDto, institutionUiConfig);

        assertNull(whitelistingInfoText);
    }

    @Test
    void getChallengeInfoText_Success_Null() {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();

        String challengeInfoText =
                institutionUiParamsMapper.getChallengeInfoText(
                        transaction, institutionUiConfig, authConfigDto, UIType.TEXT, null);

        assertNotNull(challengeInfoText);
        assertEquals(
                "An OTP has been sent to mobile number ending with 7890 successfully!",
                challengeInfoText);
    }

    @Test
    void getChallengeInfoText_Success_Resend() {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();

        String challengeInfoText =
                institutionUiParamsMapper.getChallengeInfoText(
                        transaction,
                        institutionUiConfig,
                        authConfigDto,
                        UIType.TEXT,
                        InternalConstants.RESEND);

        assertNotNull(challengeInfoText);
        assertEquals(
                "An OTP has been resent to mobile number ending with 7890 successfully!",
                challengeInfoText);
    }

    @Test
    void getChallengeInfoText_Success_Otp_Validation() {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();

        String challengeInfoText =
                institutionUiParamsMapper.getChallengeInfoText(
                        transaction,
                        institutionUiConfig,
                        authConfigDto,
                        UIType.TEXT,
                        InternalConstants.VALIDATE_OTP);

        assertNotNull(challengeInfoText);
        assertEquals("Incorrect OTP, you have 2 attempts left!", challengeInfoText);
    }

    @ParameterizedTest
    @CsvSource({"01", "02", "03"})
    void getChallengeSelectInfo_Success(String uiType) {

        when(testConfigProperties.isEnable()).thenReturn(true);

        ChallengeSelectInfo[] challengeSelectInfos =
                institutionUiParamsMapper.getChallengeSelectInfo(
                        testConfigProperties, UIType.getUIType(uiType));

        if (uiType.equals(UIType.TEXT.getType())) {
            assertNull(challengeSelectInfos);
        } else {
            assertNotNull(challengeSelectInfos);
            assertEquals("yes", challengeSelectInfos[0].getYes());
            assertEquals("no", challengeSelectInfos[1].getYes());
        }
    }

    @ParameterizedTest
    @CsvSource({InternalConstants.RESEND, InternalConstants.VALIDATE_OTP})
    void getResendAttemptLeft_Success(String currentFlowType) {

        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);

        String resendAttemptLeft =
                institutionUiParamsMapper.getResendAttemptLeft(
                        transaction, authConfigDto, currentFlowType);

        if (currentFlowType.equals(InternalConstants.VALIDATE_OTP)) {
            assertNull(resendAttemptLeft);
        } else {
            assertNotNull(resendAttemptLeft);
            assertEquals("2", resendAttemptLeft);
        }
    }

    @ParameterizedTest
    @CsvSource({InternalConstants.RESEND, InternalConstants.VALIDATE_OTP})
    void getOtpAttemptLeft_Success(String currentFlowType) {

        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);

        String otpAttemptLeft =
                institutionUiParamsMapper.getOtpAttemptLeft(
                        transaction, authConfigDto, currentFlowType);

        if (currentFlowType.equals(InternalConstants.RESEND)) {
            assertNull(otpAttemptLeft);
        } else {
            assertNotNull(otpAttemptLeft);
            assertEquals("2", otpAttemptLeft);
        }
    }
}
