package org.freedomfinancestack.razorpay.cas.acs.data;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public class ChallengeFlowDtoTestData {

    public static ChallengeFlowDto createChallengeFlowDto(
            Transaction transaction, CRES cres, String currentFlowType) {
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        challengeFlowDto.setSendRreq(false);
        challengeFlowDto.setAuthValue("0000");
        challengeFlowDto.setErrorResponse(null);
        challengeFlowDto.setCres(cres);
        challengeFlowDto.setThreeDSSessionData("");
        challengeFlowDto.setNotificationUrl("");
        challengeFlowDto.setSendEmptyResponse(true);
        challengeFlowDto.setEncryptedResponse("");
        challengeFlowDto.setInstitutionUIParams(null);
        challengeFlowDto.setTransaction(transaction);
        challengeFlowDto.setCurrentFlowType(currentFlowType);
        return challengeFlowDto;
    }
}
