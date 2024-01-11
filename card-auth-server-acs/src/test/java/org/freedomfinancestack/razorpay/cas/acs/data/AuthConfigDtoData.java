package org.freedomfinancestack.razorpay.cas.acs.data;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;
import org.freedomfinancestack.razorpay.cas.dao.model.*;

public class AuthConfigDtoData {
    public static AuthConfigDto createAuthConfigDto(OOBType oobType){
        AuthConfigDto authConfigDto= new AuthConfigDto();
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
//        challengeAuthTypeConfig.setDefaultAuthType();
        return null;
    }

    private static OOBConfig createOOBConfig(OOBType oobType) {
        OOBConfig oobConfig = new OOBConfig();
        oobConfig.setOobType(oobType);
        return oobConfig;
    }
}
