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

    public static AuthConfigDto createAuthConfigDto(OOBType oobType) {
        AuthConfigDto authConfigDto = new AuthConfigDto();
        authConfigDto.setOobConfig(createOOBConfig(oobType));
        authConfigDto.setOtpConfig(new OtpConfig(4));
        authConfigDto.setPasswordConfig(new PasswordConfig());
        authConfigDto.setChallengeAuthTypeConfig(createChallengeAuthType());
        authConfigDto.setChallengeAttemptConfig(createChallengeAttemptConfig());
        return authConfigDto;
    }

    private static ChallengeAttemptConfig createChallengeAttemptConfig() {
        ChallengeAttemptConfig challengeAttemptConfig = new ChallengeAttemptConfig();
        challengeAttemptConfig.setAttemptThreshold(3);
        challengeAttemptConfig.setResendThreshold(3);
        challengeAttemptConfig.setBlockOnExceedAttempt(true);
        challengeAttemptConfig.setWhitelistingAllowed(true);
        return challengeAttemptConfig;
    }

    private static ChallengeAuthTypeConfig createChallengeAuthType() {
        ChallengeAuthTypeConfig challengeAuthTypeConfig = new ChallengeAuthTypeConfig();
        challengeAuthTypeConfig.setThreshold(BigDecimal.valueOf(2000));
        challengeAuthTypeConfig.setDefaultAuthType(AuthType.OTP);
        challengeAuthTypeConfig.setThresholdAuthType(AuthType.UNKNOWN);

        return challengeAuthTypeConfig;
    }

    private static OOBConfig createOOBConfig(OOBType oobType) {
        OOBConfig oobConfig = new OOBConfig();
        oobConfig.setOobType(oobType);
        return oobConfig;
    }
}
