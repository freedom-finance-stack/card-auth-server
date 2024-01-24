package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.data.ChallengeFlowDtoTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.UiParamsTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.InstitutionUiParamsMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ImageProcessingException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.UiConfigException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.ThymeleafService;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionUiConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstitutionUiServiceImplTest {
    @Mock private InstitutionUiConfigRepository institutionUiConfigRepository;

    @Mock private InstitutionUiConfiguration institutionUiConfiguration;

    @Mock private ThymeleafService thymeleafService;

    @Mock private InstitutionUiParamsMapper institutionUiParamsMapper;

    @InjectMocks private InstitutionUiServiceImpl institutionUiService;

    @Test
    void testPopulateUiParams_Failure_Ui_Config_Not_Found()
            throws UiConfigException, ImageProcessingException {
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        CRES cres = new CRES();
        ChallengeFlowDto challengeFlowDto =
                ChallengeFlowDtoTestData.createChallengeFlowDto(transaction, cres, null);

        when(institutionUiConfigRepository.findById(any())).thenReturn(Optional.empty());

        UiConfigException exception =
                assertThrows(
                        UiConfigException.class,
                        () ->
                                institutionUiService.populateUiParams(
                                        challengeFlowDto, authConfigDto));

        assertEquals(InternalErrorCode.INSTITUTION_UI_CONFIG_NOT_FOUND, exception.getErrorCode());
        assertEquals("Institution Ui Config not found", exception.getMessage());
    }

    @Test
    void testPopulateUiParams_Success_Non_HTML_OTHERS()
            throws UiConfigException, ImageProcessingException {
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        CRES cres = new CRES();
        ChallengeFlowDto challengeFlowDto =
                ChallengeFlowDtoTestData.createChallengeFlowDto(transaction, cres, null);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);

        when(institutionUiConfigRepository.findById(any()))
                .thenReturn(Optional.of(institutionUiConfig));
        when(institutionUiParamsMapper.toInstitutionUiParams(
                        any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(institutionUIParams);

        institutionUiService.populateUiParams(challengeFlowDto, authConfigDto);

        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
    }

    @Test
    void testPopulateUiParams_Failure_HTML_OTHERS()
            throws UiConfigException, ImageProcessingException {
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionSdkDetail().setAcsInterface("02");
        CRES cres = new CRES();
        ChallengeFlowDto challengeFlowDto =
                ChallengeFlowDtoTestData.createChallengeFlowDto(transaction, cres, null);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);

        when(institutionUiConfigRepository.findById(any()))
                .thenReturn(Optional.of(institutionUiConfig));
        when(institutionUiParamsMapper.toInstitutionUiParams(
                        any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(institutionUIParams);
        when(thymeleafService.getOtpHTMLPage(any(), any())).thenReturn(null);

        UiConfigException exception =
                assertThrows(
                        UiConfigException.class,
                        () ->
                                institutionUiService.populateUiParams(
                                        challengeFlowDto, authConfigDto));

        assertEquals(InternalErrorCode.DISPLAY_PAGE_NOT_FOUND, exception.getErrorCode());
        assertEquals("can not create html display page", exception.getMessage());
    }

    @Test
    void testPopulateUiParams_Success_HTML_OTHERS()
            throws UiConfigException, ImageProcessingException {
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionSdkDetail().setAcsInterface("02");
        CRES cres = new CRES();
        ChallengeFlowDto challengeFlowDto =
                ChallengeFlowDtoTestData.createChallengeFlowDto(transaction, cres, null);
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();
        InstitutionUIParams institutionUIParams =
                UiParamsTestData.createInstitutionUiParams(transaction, authConfigDto);

        when(institutionUiConfigRepository.findById(any()))
                .thenReturn(Optional.of(institutionUiConfig));
        when(institutionUiParamsMapper.toInstitutionUiParams(
                        any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(institutionUIParams);
        when(thymeleafService.getOtpHTMLPage(any(), any()))
                .thenReturn(UiParamsTestData.SAMPLE_DISPLAY_PAGE);

        institutionUiService.populateUiParams(challengeFlowDto, authConfigDto);

        assertNotNull(challengeFlowDto.getInstitutionUIParams());
        assertTrue(challengeFlowDto.getInstitutionUIParams().isTest());
        assertNotNull(challengeFlowDto.getInstitutionUIParams().getDisplayPage());
        assertEquals(
                UiParamsTestData.SAMPLE_ENCODED_DISPLAY_PAGE,
                challengeFlowDto.getInstitutionUIParams().getDisplayPage());
    }
}
