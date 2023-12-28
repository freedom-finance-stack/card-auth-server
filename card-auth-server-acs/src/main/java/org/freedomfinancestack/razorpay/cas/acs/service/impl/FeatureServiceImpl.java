package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.FeatureService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.DeviceRenderOptions;
import org.freedomfinancestack.razorpay.cas.dao.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.model.*;
import org.freedomfinancestack.razorpay.cas.dao.repository.FeatureRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code FeatureServiceImpl} class is an implementation of the {@link FeatureService}
 * interface. This service is responsible for fetching the authentication configuration for a given
 * set of card range, ground and institution ids. If config exist under multiple ids provided it
 * will give precedence in following order CardRange, CardRangeGroup, Institution
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;

    @Override
    public void getACSRenderingType(
            Transaction transaction, DeviceRenderOptions deviceRenderOptions)
            throws ACSDataAccessException {
        RenderingTypeConfigList renderingTypeConfigList =
                (RenderingTypeConfigList)
                        featureRepository.findFeatureByIds(
                                FeatureName.RENDERING_TYPE, getEntityIdsByType(transaction));
        // todo handle renderingTypeConfigList is null
        RenderingTypeConfig finalRenderingTypeConfig = null;

        for (RenderingTypeConfig renderingTypeConfig :
                renderingTypeConfigList.getRenderingTypeConfigs()) {
            String acsUiType =
                    Util.findFirstCommonString(
                            Arrays.asList(deviceRenderOptions.getSdkUiType()),
                            renderingTypeConfig.getAcsUiTemplate());

            if (acsUiType == null) {
                continue;
            }
            if (renderingTypeConfig
                    .getAcsInterface()
                    .equals(deviceRenderOptions.getSdkInterface())) {
                transaction
                        .getTransactionSdkDetail()
                        .setAcsInterface(renderingTypeConfig.getAcsInterface());
                transaction.getTransactionSdkDetail().setAcsUiTemplate(acsUiType);
                return;
            } else if (deviceRenderOptions.getSdkInterface().equals("03")) {
                if (finalRenderingTypeConfig == null
                        || finalRenderingTypeConfig.getPreference()
                                > renderingTypeConfig.getPreference()) {
                    finalRenderingTypeConfig = renderingTypeConfig;
                    transaction
                            .getTransactionSdkDetail()
                            .setAcsInterface(renderingTypeConfig.getAcsInterface());
                    transaction.getTransactionSdkDetail().setAcsUiTemplate(acsUiType);
                }
            }
        }
        if (finalRenderingTypeConfig != null) {
            return;
        }

        if (renderingTypeConfigList.getRenderingTypeConfigs() != null
                && renderingTypeConfigList.getRenderingTypeConfigs().size() > 0) {
            log.error(
                    "Rendering Type mismatch for Institution ID : "
                            + transaction.getInstitutionId()
                            + " and Card Range ID : "
                            + transaction.getCardRangeId());
            throw new ACSDataAccessException(
                    InternalErrorCode.UNSUPPPORTED_DEVICE_CATEGORY,
                    "Matching Rendering Type Config not found");
        }

        log.error(
                "Rendering Type not found for Institution ID : "
                        + transaction.getInstitutionId()
                        + " and Card Range ID : "
                        + transaction.getCardRangeId());
        throw new ACSDataAccessException(
                InternalErrorCode.RENDERING_TYPE_NOT_FOUND, "Rendering Type Config not found");
    }

    @Override
    public AuthConfigDto getAuthenticationConfig(Transaction transaction)
            throws ACSDataAccessException {
        return getAuthenticationConfig(getEntityIdsByType(transaction));
    }

    // todo add cache
    @Override
    public AuthConfigDto getAuthenticationConfig(Map<FeatureEntityType, String> entityIdsByType)
            throws ACSDataAccessException {
        AuthConfigDto authConfigDto = new AuthConfigDto();
        ChallengeAuthTypeConfig challengeAuthTypeConfig =
                (ChallengeAuthTypeConfig)
                        featureRepository.findFeatureByIds(
                                FeatureName.CHALLENGE_AUTH_TYPE, entityIdsByType);
        if (challengeAuthTypeConfig == null) {
            throw new ACSDataAccessException(
                    InternalErrorCode.AUTH_CONFIG_NOT_PRESENT,
                    "Challenge Auth Type Config not found");
        }
        authConfigDto.setChallengeAuthTypeConfig(challengeAuthTypeConfig);
        ChallengeAttemptConfig challengeAttemptConfig =
                (ChallengeAttemptConfig)
                        featureRepository.findFeatureByIds(
                                FeatureName.CHALLENGE_ATTEMPT, entityIdsByType);
        if (challengeAttemptConfig == null) {
            throw new ACSDataAccessException(
                    InternalErrorCode.AUTH_CONFIG_NOT_PRESENT,
                    "Challenge attempt Config not found");
        }
        authConfigDto.setChallengeAttemptConfig(challengeAttemptConfig);

        fetchAuthTypeConfig(
                challengeAuthTypeConfig.getDefaultAuthType(), authConfigDto, entityIdsByType);
        fetchAuthTypeConfig(
                challengeAuthTypeConfig.getThresholdAuthType(), authConfigDto, entityIdsByType);

        return authConfigDto;
    }

    private Map<FeatureEntityType, String> getEntityIdsByType(Transaction transaction) {
        Map<FeatureEntityType, String> entityIdsByType = new HashMap<>();
        entityIdsByType.put(FeatureEntityType.INSTITUTION, transaction.getInstitutionId());
        entityIdsByType.put(FeatureEntityType.CARD_RANGE, transaction.getCardRangeId());
        return entityIdsByType;
    }

    private void fetchAuthTypeConfig(
            AuthType authType,
            AuthConfigDto authConfigDto,
            Map<FeatureEntityType, String> entityIdsByType)
            throws ACSDataAccessException {
        switch (authType) {
            case OTP -> {
                OtpConfig otpConfig =
                        (OtpConfig)
                                featureRepository.findFeatureByIds(
                                        FeatureName.OTP, entityIdsByType);
                if (otpConfig == null) {
                    throw new ACSDataAccessException(
                            InternalErrorCode.AUTH_CONFIG_NOT_PRESENT, "OTP Config not found");
                }
                authConfigDto.setOtpConfig(otpConfig);
            }
            case PASSWORD -> {
                PasswordConfig passwordConfig =
                        (PasswordConfig)
                                featureRepository.findFeatureByIds(
                                        FeatureName.PASSWORD, entityIdsByType);
                if (passwordConfig == null) {
                    throw new ACSDataAccessException(
                            InternalErrorCode.AUTH_CONFIG_NOT_PRESENT, "Password Config not found");
                }
                authConfigDto.setPasswordConfig(passwordConfig);
            }
            case Decoupled -> log.info("CONFIG FOR DECOUPLED IS YET TO ADD");
            default -> throw new ACSDataAccessException(
                    InternalErrorCode.AUTH_CONFIG_NOT_PRESENT, "Invalid Auth Type");
        }
    }
}
