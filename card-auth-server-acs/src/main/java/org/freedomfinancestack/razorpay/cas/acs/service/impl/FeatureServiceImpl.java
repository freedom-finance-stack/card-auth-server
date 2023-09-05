package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.FeatureService;
import org.freedomfinancestack.razorpay.cas.dao.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.model.*;
import org.freedomfinancestack.razorpay.cas.dao.repository.FeatureRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;

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