package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.UiParamsTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ImageProcessingException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstitutionUiParamsMapperImplTest {

    @Mock private AppConfiguration appConfiguration;

    @Mock private InstitutionUiConfiguration institutionUiConfiguration;

    @Mock private TestConfigProperties testConfigProperties;

    @InjectMocks private InstitutionUiParamsMapperImpl institutionUiParamsMapperImpl;

    @Test
    void toInstitutionUiParams_Transaction_Null() throws ImageProcessingException {

        InstitutionUIParams institutionUIParams =
                institutionUiParamsMapperImpl.toInstitutionUiParams(
                        null, null, null, null, null, null, null, null);

        assertNull(institutionUIParams);
    }

    @Test
    void toInstitutionUiParams_Success() throws ImageProcessingException {

        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        InstitutionUiConfig institutionUiConfig = UiParamsTestData.createInstitutionUiConfig();
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, false, AuthType.OTP, AuthType.UNKNOWN);

        InstitutionUiParamsMapperImpl institutionUiParamsMapper =
                mock(InstitutionUiParamsMapperImpl.class);

        when(testConfigProperties.isEnable()).thenReturn(true);
        when(appConfiguration.getHostname()).thenReturn("http://localhost:8080");

        String logoUrl = "https://drive.google.com/uc?id=1lYxWs3uk_PpV7TAL2MGEwQ1uFAEaxqLM";
        when(institutionUiConfiguration.getMediumLogo()).thenReturn(logoUrl);
        when(institutionUiConfiguration.getHighLogo()).thenReturn(logoUrl);
        when(institutionUiConfiguration.getExtraHighLogo()).thenReturn(logoUrl);

        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
        InstitutionUiConfiguration.UiConfig uiConfig = new InstitutionUiConfiguration.UiConfig();
        uiConfig.setMediumPs(logoUrl);
        uiConfig.setHighPs(logoUrl);
        uiConfig.setExtraHighPs(logoUrl);
        Map<Network, InstitutionUiConfiguration.UiConfig> networkUiConfigMap =
                new EnumMap<>(Network.class);
        networkUiConfigMap.put(network, uiConfig);
        when(institutionUiConfiguration.getNetworkUiConfig()).thenReturn(networkUiConfigMap);

        InstitutionUIParams institutionUIParams =
                institutionUiParamsMapperImpl.toInstitutionUiParams(
                        transaction,
                        institutionUiConfig,
                        authConfigDto,
                        UIType.TEXT,
                        InternalConstants.RESEND,
                        appConfiguration,
                        institutionUiConfiguration,
                        testConfigProperties);

        assertNotNull(institutionUIParams);
        assertNotNull(institutionUIParams.getAcsTransID());
        assertEquals(
                "An OTP has been resent to mobile number ending with 7890 successfully!",
                institutionUIParams.getChallengeInfoText());
        assertEquals(
                "http://localhost:8080/v2/transaction/challenge/app",
                institutionUIParams.getValidationUrl());
        assertTrue(institutionUIParams.isTest());
    }
}
