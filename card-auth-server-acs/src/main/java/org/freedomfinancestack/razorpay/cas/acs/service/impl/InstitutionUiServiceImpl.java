package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.InstitutionUiParamsMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ImageProcessingException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.UiConfigException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.InstitutionUiService;
import org.freedomfinancestack.razorpay.cas.acs.service.ThymeleafService;
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfigPK;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionUiConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "InstitutionUiServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionUiServiceImpl implements InstitutionUiService {

    private final InstitutionUiConfigRepository institutionUiConfigRepository;
    private final AppConfiguration appConfiguration;
    private final InstitutionUiConfiguration institutionUiConfiguration;
    private final TestConfigProperties testConfigProperties;
    private final ThymeleafService thymeleafService;
    private final InstitutionUiParamsMapper institutionUiParamsMapper;

    @Override
    public void populateUiParams(ChallengeFlowDto challengeFlowDto, AuthConfigDto authConfigDto)
            throws UiConfigException, ImageProcessingException {

        AuthType authType =
                AuthType.getAuthType(challengeFlowDto.getTransaction().getAuthenticationType());

        UIType uiType =
                challengeFlowDto
                                .getTransaction()
                                .getDeviceChannel()
                                .equals(DeviceChannel.BRW.getChannel())
                        ? UIType.TEXT
                        : UIType.getUIType(
                                challengeFlowDto
                                        .getTransaction()
                                        .getTransactionSdkDetail()
                                        .getAcsUiTemplate());
        Optional<InstitutionUiConfig> institutionUiConfig =
                institutionUiConfigRepository.findById(
                        new InstitutionUiConfigPK(
                                challengeFlowDto.getTransaction().getInstitutionId(),
                                authType,
                                uiType));

        if (institutionUiConfig.isPresent()) {
            populateUiParamsHandler(
                    challengeFlowDto, institutionUiConfig.get(), authConfigDto, uiType);
            return;
        }

        log.error(
                "Institution Ui Config not found for Institution ID : "
                        + challengeFlowDto.getTransaction().getInstitutionId());
        throw new UiConfigException(
                InternalErrorCode.INSTITUTION_UI_CONFIG_NOT_FOUND,
                "Institution Ui Config not found");
    }

    private void populateUiParamsHandler(
            ChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto,
            UIType uiType)
            throws UiConfigException, ImageProcessingException {

        InstitutionUIParams validInstitutionUIParams =
                institutionUiParamsMapper.toInstitutionUiParams(
                        challengeFlowDto.getTransaction(),
                        institutionUiConfig,
                        authConfigDto,
                        uiType,
                        challengeFlowDto.getCurrentFlowType(),
                        appConfiguration,
                        institutionUiConfiguration,
                        testConfigProperties);

        if (challengeFlowDto
                        .getTransaction()
                        .getDeviceChannel()
                        .equals(DeviceChannel.APP.getChannel())
                && challengeFlowDto
                        .getTransaction()
                        .getTransactionSdkDetail()
                        .getAcsInterface()
                        .equals(DeviceInterface.HTML.getValue())) {
            validInstitutionUIParams.setDisplayPage(getEncodedHtml(validInstitutionUIParams));
        }
        challengeFlowDto.setInstitutionUIParams(validInstitutionUIParams);
    }

    private String getEncodedHtml(InstitutionUIParams institutionUIParams)
            throws UiConfigException {
        String html;
        html =
                thymeleafService.getOtpHTMLPage(
                        institutionUIParams, institutionUiConfiguration.getHtmlOtpTemplate());

        if (html == null) {
            throw new UiConfigException(
                    InternalErrorCode.DISPLAY_PAGE_NOT_FOUND, "can not create html display page");
        }
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(html.getBytes(StandardCharsets.UTF_8));
    }
}
