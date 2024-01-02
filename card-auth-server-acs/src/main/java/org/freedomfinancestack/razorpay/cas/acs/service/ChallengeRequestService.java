package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

/**
 * The {@code ChallengeRequestService} interface represents a service responsible for processing
 * challenge request (CReq) and generating challenge response (CRes) for 3D Secure transactions. The
 * service handles the validation of CReq, communication with the DS (Directory Service), and
 * generation of the appropriate CRes based on the transaction status and other parameters.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ChallengeRequestService {

    /**
     * Process the challenge request (CReq) and generate the challenge display response (CRes) for
     * the browser based and app based request
     *
     * @param flowType device channel flow app/brw
     * @param strCReq challenge request in serialized/encrypted format
     * @param threeDSSessionData session data from requester
     * @return ChallengeFlowDto challenge response dto
     */
    ChallengeFlowDto processChallengeRequest(
            DeviceChannel flowType, String strCReq, String threeDSSessionData)
            throws ThreeDSException;

    static boolean isChallengeCompleted(Transaction transaction) {
        return transaction != null
                && !transaction.getTransactionStatus().equals(TransactionStatus.CHALLENGE_REQUIRED)
                && !transaction.getTransactionStatus().equals(TransactionStatus.CREATED)
                && !transaction
                        .getTransactionStatus()
                        .equals(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED);
    }

    static boolean isMessageVersion210ResendCondition(CREQ creq) {
        return (creq.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_1_0)
                && !creq.getSdkCounterStoA().equals(InternalConstants.INITIAL_ACS_SDK_COUNTER)
                && (creq.getResendChallenge() == null
                        || InternalConstants.NO.equals(creq.getResendChallenge()))
                && creq.getChallengeDataEntry() == null
                && creq.getChallengeHTMLDataEntry() == null
                && creq.getOobContinue() == null
                && creq.getChallengeCancel() == null);
    }
}
