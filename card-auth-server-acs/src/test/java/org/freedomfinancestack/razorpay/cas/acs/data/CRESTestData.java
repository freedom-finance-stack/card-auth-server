package org.freedomfinancestack.razorpay.cas.acs.data;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public class CRESTestData {

    // TODO: implement MessageExtension
    public static CRES createCRES(
            Transaction transaction, InstitutionUIParams institutionUIParams) {
        CRES cres = new CRES();

        cres.setThreeDSServerTransID(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId());
        cres.setAcsTransID(transaction.getId());
        cres.setMessageType(MessageType.CRes.toString());
        cres.setMessageVersion(transaction.getMessageVersion());

        if (transaction.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            cres.setAcsCounterAtoS(transaction.getTransactionSdkDetail().getAcsCounterAtoS());
            cres.setAcsUiType(transaction.getTransactionSdkDetail().getAcsUiType());
            cres.setChallengeCompletionInd(
                    ChallengeRequestService.isChallengeCompleted(transaction)
                            ? InternalConstants.YES
                            : InternalConstants.NO);
            cres.setSdkTransID(transaction.getTransactionSdkDetail().getSdkTransId());
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.NATIVE.getValue())) {
                cres.setPsImage(institutionUIParams.getPsImage());
                cres.setIssuerImage(institutionUIParams.getIssuerImage());
                cres.setChallengeInfoHeader(institutionUIParams.getChallengeInfoHeader());
                cres.setChallengeInfoText(institutionUIParams.getChallengeInfoText());
                cres.setChallengeInfoLabel(institutionUIParams.getChallengeInfoText());
                cres.setChallengeSelectInfo(institutionUIParams.getChallengeSelectInfo());
                cres.setExpandInfoLabel(institutionUIParams.getExpandInfoLabel());
                cres.setExpandInfoText(institutionUIParams.getExpandInfoText());
                cres.setResendInformationLabel(institutionUIParams.getResendInformationLabel());
                cres.setSubmitAuthenticationLabel(
                        institutionUIParams.getSubmitAuthenticationLabel());
                cres.setWhyInfoLabel(institutionUIParams.getWhyInfoLabel());
                cres.setWhyInfoText(institutionUIParams.getWhyInfoText());
                cres.setWhitelistingInfoText(institutionUIParams.getWhitelistingInfoText());
                cres.setOobContinueLabel(institutionUIParams.getOobContinueLabel());
            } else {
                cres.setAcsHTML(institutionUIParams.getDisplayPage());
            }
        }
        return cres;
    }

    public static CRES createFinalCRes(Transaction transaction) {
        CRES cres = new CRES();

        cres.setThreeDSServerTransID(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId());
        cres.setAcsTransID(transaction.getId());
        cres.setMessageType(MessageType.CRes.toString());
        cres.setMessageVersion(transaction.getMessageVersion());
        cres.setTransStatus(transaction.getTransactionStatus().getStatus());

        if (transaction.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            cres.setSdkTransID(transaction.getTransactionSdkDetail().getSdkTransId());
            cres.setAcsCounterAtoS(transaction.getTransactionSdkDetail().getAcsCounterAtoS());
            cres.setChallengeCompletionInd(
                    ChallengeRequestService.isChallengeCompleted(transaction)
                            ? InternalConstants.YES
                            : InternalConstants.NO);
        }
        return cres;
    }
}
