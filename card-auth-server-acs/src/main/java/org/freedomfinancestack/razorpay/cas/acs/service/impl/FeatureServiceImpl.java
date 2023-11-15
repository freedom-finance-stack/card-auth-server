package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.FeatureService;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
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
    public RenderingTypeConfig getRenderingTypeConfig(
            Transaction transaction, DeviceInterface deviceInterface, boolean fetchDefault)
            throws ACSDataAccessException {
        List<RenderingTypeConfig> renderingTypeConfigs =
                (List<RenderingTypeConfig>)
                        featureRepository.findFeatureByIds(
                                FeatureName.RENDERING_TYPE, getEntityIdsByType(transaction));

        for (RenderingTypeConfig renderingTypeConfig : renderingTypeConfigs) {
            if (renderingTypeConfig.getAcsInterface().equals(deviceInterface.getValue())) {
                if (fetchDefault) {
                    if (renderingTypeConfig.getDefaultRenderOption().equals("1")) {
                        return renderingTypeConfig;
                    }
                } else {
                    return renderingTypeConfig;
                }
            }
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
                    InternalErrorCode.INVALID_CONFIG, "Challenge Auth Type Config not found");
        }
        authConfigDto.setChallengeAuthTypeConfig(challengeAuthTypeConfig);
        ChallengeAttemptConfig challengeAttemptConfig =
                (ChallengeAttemptConfig)
                        featureRepository.findFeatureByIds(
                                FeatureName.CHALLENGE_ATTEMPT, entityIdsByType);
        if (challengeAttemptConfig == null) {
            throw new ACSDataAccessException(
                    InternalErrorCode.INVALID_CONFIG, "Challenge attempt Config not found");
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
            case OTP:
                OtpConfig otpConfig =
                        (OtpConfig)
                                featureRepository.findFeatureByIds(
                                        FeatureName.OTP, entityIdsByType);
                if (otpConfig == null) {
                    throw new ACSDataAccessException(
                            InternalErrorCode.INVALID_CONFIG, "OTP Config not found");
                }
                authConfigDto.setOtpConfig(otpConfig);
                break;
            case PASSWORD:
                PasswordConfig passwordConfig =
                        (PasswordConfig)
                                featureRepository.findFeatureByIds(
                                        FeatureName.PASSWORD, entityIdsByType);
                if (passwordConfig == null) {
                    throw new ACSDataAccessException(
                            InternalErrorCode.INVALID_CONFIG, "Password Config not found");
                }
                authConfigDto.setPasswordConfig(passwordConfig);
                break;
            default:
                throw new ACSDataAccessException(
                        InternalErrorCode.INVALID_CONFIG, "Invalid Auth Type");
        }
    }
}
