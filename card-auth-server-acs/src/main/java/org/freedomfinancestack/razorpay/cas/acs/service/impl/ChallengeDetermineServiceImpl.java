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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChallengeDetermineServiceImpl implements ChallengeDetermineService {

    private RiskFlag isChallengeRequired(final AREQ objAReq, final RiskFlag riskFlagByAcs) {

        RiskFlag riskFlag = null;
        if (!Util.isNullorBlank(objAReq.getThreeDSRequestorChallengeInd())) {
            ThreeDSRequestorChallengeInd challengeInd =
                    ThreeDSRequestorChallengeInd.getByValue(
                            objAReq.getThreeDSRequestorChallengeInd());
            ThreeDSReqAuthMethodInd methodInd =
                    ThreeDSReqAuthMethodInd.getByValue(objAReq.getThreeDSReqAuthMethodInd());
            if (ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_MANDATE.equals(challengeInd)
                    || ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_REQUESTER_PREFERENCE.equals(
                            challengeInd)
                    || ThreeDSRequestorChallengeInd.WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                            .equals(challengeInd)) {
                riskFlag = RiskFlag.CHALLENGE;
            } else if (!Util.isNullorBlank(objAReq.getAcctInfo())
                    && !Util.isNullorBlank(objAReq.getAcctInfo().getSuspiciousAccActivity())
                    && "02".equals(objAReq.getAcctInfo().getSuspiciousAccActivity())) {
                riskFlag = RiskFlag.CHALLENGE;
            } else if (!Util.isNullorBlank(objAReq.getThreeDSReqAuthMethodInd())
                    && (ThreeDSReqAuthMethodInd.FAILED.equals(methodInd)
                            || ThreeDSReqAuthMethodInd.NOT_PERFORMED.equals(methodInd))) {
                riskFlag = RiskFlag.CHALLENGE;
            } else if (ThreeDSRequestorChallengeInd.TRANSACTIONAL_RISK_ANALYSIS_IS_ALREADY_PERFORMED
                    .equals(challengeInd)) {
                riskFlag = RiskFlag.NO_CHALLENGE;
            } else if (!Util.isNullorBlank(objAReq.getThreeDSRequestorDecReqInd())
                    && objAReq.getThreeDSRequestorDecReqInd().equalsIgnoreCase("Y")
                    && riskFlagByAcs.equals(RiskFlag.DECOUPLED_CHALLENGE)) {
                riskFlag = RiskFlag.DECOUPLED_CHALLENGE;
            } else if (ThreeDSRequestorChallengeInd.DATA_SHARE_ONLY.equals(challengeInd)) {
                if (riskFlagByAcs.equals(RiskFlag.INFORMATIONAL)) {
                    riskFlag = RiskFlag.INFORMATIONAL;
                } else {
                    riskFlag = RiskFlag.NO_CHALLENGE;
                }
            }
        }
        if (riskFlag == null) {
            riskFlag = riskFlagByAcs;
        }
        return riskFlag;
    }

    @Override
    public void determineChallenge(
            final AREQ objAReq, final Transaction transaction, final RiskFlag riskFlagAcs) {

        RiskFlag riskFlag = isChallengeRequired(objAReq, riskFlagAcs);
        if (RiskFlag.CHALLENGE == riskFlag) {
            transaction.setChallengeMandated(true);
            transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        } else if (RiskFlag.RBA == riskFlag) {
            // todo add risk engine here
            transaction.setChallengeMandated(true);
            transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        } else if (RiskFlag.DECOUPLED_CHALLENGE == riskFlag) {
            if (InternalConstants.YES.equals(objAReq.getThreeDSRequestorDecReqInd())
                    && riskFlagAcs.equals(RiskFlag.DECOUPLED_CHALLENGE)) {
                transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED);
                transaction.setChallengeMandated(true);
            } else {
                transaction.setTransactionStatus(
                        TransactionStatus.SUCCESS); // Marking it successful for portal testing.
                transaction.setChallengeMandated(false);
            }
        } else if (RiskFlag.INFORMATIONAL == riskFlag) {
            transaction.setTransactionStatus(TransactionStatus.INFORMATIONAL);
            transaction.setChallengeMandated(false);
        } else if (RiskFlag.NO_CHALLENGE == riskFlag) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            transaction.setChallengeMandated(false);
        }
    }
}
