package org.freedomfinancestack.razorpay.cas.acs.data;

import java.math.BigDecimal;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;
import org.freedomfinancestack.razorpay.cas.dao.model.*;

public class AuthConfigDtoData {
    public static AuthenticationDto createAuthenticationDto(
            AuthConfigDto authConfigDto, Transaction transaction, String authValue) {
        return AuthenticationDto.builder()
                .authConfigDto(authConfigDto)
                .transaction(transaction)
                .authValue(authValue)
                .build();
    }

    public static AuthConfigDto createAuthConfigDto(
            OOBType oobType,
            boolean blockOnExceedAttempt,
            boolean whitelistingAllowed,
            AuthType defaultAuthType,
            AuthType thresholdAuthType) {
        AuthConfigDto authConfigDto = new AuthConfigDto();
        authConfigDto.setOobConfig(createOOBConfig(oobType));
        authConfigDto.setOtpConfig(new OtpConfig(4));
        authConfigDto.setPasswordConfig(new PasswordConfig());
        authConfigDto.setChallengeAuthTypeConfig(
                createChallengeAuthType(defaultAuthType, thresholdAuthType));
        authConfigDto.setChallengeAttemptConfig(
                createChallengeAttemptConfig(blockOnExceedAttempt, whitelistingAllowed));
        return authConfigDto;
    }

    public static ChallengeAttemptConfig createChallengeAttemptConfig(
            boolean blockOnExceedAttempt, boolean whitelistingAllowed) {
        ChallengeAttemptConfig challengeAttemptConfig = new ChallengeAttemptConfig();
        challengeAttemptConfig.setAttemptThreshold(3);
        challengeAttemptConfig.setResendThreshold(3);
        challengeAttemptConfig.setBlockOnExceedAttempt(blockOnExceedAttempt);
        challengeAttemptConfig.setWhitelistingAllowed(whitelistingAllowed);
        return challengeAttemptConfig;
    }

    public static ChallengeAuthTypeConfig createChallengeAuthType(
            AuthType defaultAuthType, AuthType thresholdAuthType) {
        ChallengeAuthTypeConfig challengeAuthTypeConfig = new ChallengeAuthTypeConfig();
        challengeAuthTypeConfig.setThreshold(BigDecimal.valueOf(2000));
        challengeAuthTypeConfig.setDefaultAuthType(defaultAuthType);
        challengeAuthTypeConfig.setThresholdAuthType(thresholdAuthType);

        return challengeAuthTypeConfig;
    }

    public static OOBConfig createOOBConfig(OOBType oobType) {
        OOBConfig oobConfig = new OOBConfig();
        oobConfig.setOobType(oobType);
        return oobConfig;
    }
}
