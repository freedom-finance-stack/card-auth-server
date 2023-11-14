package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeDetermineService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSReqAuthMethodInd;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSRequestorChallengeInd;
import org.freedomfinancestack.razorpay.cas.dao.enums.RiskFlag;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class ChallengeDetermineServiceImpl implements ChallengeDetermineService {

    private RiskFlag isChallengeRequired(final AREQ objAReq, final RiskFlag riskFlagByAcs) {

        RiskFlag riskFlag = null;
        if (!Util.isNullorBlank(objAReq.getThreeDSRequestorChallengeInd())) {
            ThreeDSRequestorChallengeInd challengeInd =
                    ThreeDSRequestorChallengeInd.getByValue(
                            objAReq.getThreeDSRequestorChallengeInd());
            if (ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_MANDATE.equals(challengeInd)
                    || ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_REQUESTER_PREFERENCE.equals(
                            challengeInd)
                    || ThreeDSRequestorChallengeInd.WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                            .equals(challengeInd)) {
                riskFlag = RiskFlag.CHALLENGE;
            } else if (ThreeDSRequestorChallengeInd.DATA_SHARE_ONLY.equals(challengeInd)
                    || ThreeDSRequestorChallengeInd.TRANSACTIONAL_RISK_ANALYSIS_IS_ALREADY_PERFORMED
                            .equals(challengeInd)) {
                riskFlag = RiskFlag.NO_CHALLENGE;
            } else if (!Util.isNullorBlank(objAReq.getThreeDSRequestorDecReqInd())
                    && objAReq.getThreeDSRequestorDecReqInd().equalsIgnoreCase("Y")) {
                riskFlag = RiskFlag.DECOUPLED_CHALLENGE;
            } else {
                riskFlag = riskFlagByAcs;
            }
        }
        return riskFlag;
    }

    @Override
    public void determineChallenge(
            final AREQ objAReq, final Transaction transaction, final RiskFlag riskFlagAcs) {

        RiskFlag riskFlag = isChallengeRequired(objAReq, riskFlagAcs);
        boolean challengeFlag = false;
        if (RiskFlag.CHALLENGE == riskFlag) {
            challengeFlag = true;
        } else if (RiskFlag.RBA == riskFlag) {
            if (!Util.isNullorBlank(objAReq.getThreeDSRequestorChallengeInd())) {
                ThreeDSRequestorChallengeInd challengeInd =
                        ThreeDSRequestorChallengeInd.getByValue(
                                objAReq.getThreeDSRequestorChallengeInd());
                if (ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_MANDATE.equals(challengeInd)
                        || ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_REQUESTER_PREFERENCE
                                .equals(challengeInd)
                        || ThreeDSRequestorChallengeInd.NO_PREFERENCE.equals(challengeInd)
                        || ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .equals(challengeInd)) {
                    challengeFlag = true;
                }
            } else if (Util.isNullorBlank(objAReq.getAcctInfo())
                    && Util.isNullorBlank(objAReq.getAcctInfo().getSuspiciousAccActivity())) {
                if ("02".equals(objAReq.getAcctInfo().getSuspiciousAccActivity())) {
                    challengeFlag = true;
                }
            } else if (!Util.isNullorBlank(objAReq.getThreeDSReqAuthMethodInd())) {
                ThreeDSReqAuthMethodInd methodInd =
                        ThreeDSReqAuthMethodInd.getByValue(objAReq.getThreeDSReqAuthMethodInd());
                if (ThreeDSReqAuthMethodInd.FAILED.equals(methodInd)
                        || ThreeDSReqAuthMethodInd.NOT_PERFORMED.equals(methodInd)) {
                    challengeFlag = true;
                }
            }

        } else if (RiskFlag.DECOUPLED_CHALLENGE == riskFlag) {
            if (InternalConstants.YES.equals(objAReq.getThreeDSRequestorDecReqInd())) {
                transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED);
                transaction.setChallengeMandated(true);
                transaction.setAuthenticationType(4);
            } else {
                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                transaction.setChallengeMandated(false);
            }
        }

        if (challengeFlag) {
            transaction.setChallengeMandated(true);
            transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        } else {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            transaction.setChallengeMandated(false);
        }
    }
}
