package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CResMapperTest {

    private final CResMapper cResMapper = new CResMapperImpl();

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
    }

    @Test
    void testToFinalCRes() {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        // Act
        CRES cRes = cResMapper.toFinalCRes(transaction);

        // Assert
        assertNotNull(cRes);
        assertEquals(transaction.getId(), cRes.getAcsTransID());
        assertEquals(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId(),
                cRes.getThreeDSServerTransID());
        assertEquals(
                ChallengeRequestService.isChallengeCompleted(transaction)
                        ? InternalConstants.YES
                        : InternalConstants.NO,
                cRes.getChallengeCompletionInd());
        assertEquals(transaction.getTransactionStatus().getStatus(), cRes.getTransStatus());
        assertEquals(transaction.getMessageVersion(), cRes.getMessageVersion());
        assertEquals(MessageType.CRes.toString(), cRes.getMessageType());
        assertEquals(
                DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())
                        ? transaction.getTransactionSdkDetail().getSdkTransId()
                        : null,
                cRes.getSdkTransID());
        assertEquals(
                DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())
                        ? transaction.getTransactionSdkDetail().getAcsCounterAtoS()
                        : null,
                cRes.getAcsCounterAtoS());
        assertNull(cRes.getPsImage());
        assertNull(cRes.getIssuerImage());
    }

    @Test
    void testToCRes() {
        // Arrange
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionSdkDetail().setAcsInterface(DeviceInterface.NATIVE.getValue());
        InstitutionUIParams institutionUIParams = createInstitutionUIParams();

        // Act
        CRES cRes = cResMapper.toCRes(transaction, institutionUIParams);

        // Assert
        assertNotNull(cRes);
        assertEquals(transaction.getId(), cRes.getAcsTransID());
        assertEquals(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId(),
                cRes.getThreeDSServerTransID());
        assertEquals(
                ChallengeRequestService.isChallengeCompleted(transaction)
                        ? InternalConstants.YES
                        : InternalConstants.NO,
                cRes.getChallengeCompletionInd());
        assertEquals(transaction.getMessageVersion(), cRes.getMessageVersion());
        assertEquals(MessageType.CRes.toString(), cRes.getMessageType());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsCounterAtoS(),
                cRes.getAcsCounterAtoS());
        assertEquals(transaction.getTransactionSdkDetail().getAcsUiType(), cRes.getAcsUiType());
        assertEquals(transaction.getTransactionSdkDetail().getSdkTransId(), cRes.getSdkTransID());
        assertEquals(institutionUIParams.getDisplayPage(), cRes.getAcsHTML());
        assertEquals(institutionUIParams.getPsImage(), cRes.getPsImage());
        assertEquals(institutionUIParams.getIssuerImage(), cRes.getIssuerImage());
        assertEquals(institutionUIParams.getChallengeInfoHeader(), cRes.getChallengeInfoHeader());
        assertEquals(institutionUIParams.getChallengeInfoLabel(), cRes.getChallengeInfoLabel());
        assertEquals(institutionUIParams.getChallengeInfoText(), cRes.getChallengeInfoText());
        assertEquals(institutionUIParams.getExpandInfoLabel(), cRes.getExpandInfoLabel());
        assertEquals(institutionUIParams.getExpandInfoText(), cRes.getExpandInfoText());
        assertEquals(
                institutionUIParams.getResendInformationLabel(), cRes.getResendInformationLabel());
        assertEquals(
                institutionUIParams.getSubmitAuthenticationLabel(),
                cRes.getSubmitAuthenticationLabel());
        assertEquals(institutionUIParams.getWhyInfoLabel(), cRes.getWhyInfoLabel());
        assertEquals(institutionUIParams.getWhyInfoText(), cRes.getWhyInfoText());
        assertEquals(institutionUIParams.getWhitelistingInfoText(), cRes.getWhitelistingInfoText());
        assertEquals(institutionUIParams.getOobContinueLabel(), cRes.getOobContinueLabel());
        assertEquals(institutionUIParams.getChallengeSelectInfo(), cRes.getChallengeSelectInfo());
        assertEquals(institutionUIParams.getChallengeSelectInfo(), cRes.getChallengeSelectInfo());
        assertEquals(institutionUIParams.getChallengeInfoHeader(), cRes.getChallengeInfoHeader());
    }

    public static InstitutionUIParams createInstitutionUIParams() {
        return InstitutionUIParams.builder()
                .psImage(createImage("ps-image-url"))
                .issuerImage(createImage("issuer-image-url"))
                .challengeSelectInfo(new ChallengeSelectInfo[] {createChallengeSelectInfo()})
                .challengeInfoHeader("Challenge Header")
                .challengeInfoLabel("Challenge Label")
                .challengeInfoText("Challenge Text")
                .expandInfoLabel("Expand Label")
                .expandInfoText("Expand Text")
                .submitAuthenticationLabel("Submit Authentication")
                .resendInformationLabel("Resend Information")
                .whyInfoLabel("Why Label")
                .whyInfoText("Why Text")
                .displayPage("Display Page")
                .whitelistingInfoText("Whitelisting Info")
                .oobContinueLabel("OOB Continue")
                .validationUrl("Validation URL")
                .resendBlocked("Resend Blocked")
                .acsTransID("Acs Transaction ID")
                .messageVersion("Message Version")
                .threeDSServerTransID("3DS Server Transaction ID")
                .otpAttemptLeft("3")
                .resendAttemptLeft("2")
                .merchantName("Merchant Name")
                .cardNumber("Card Number")
                .amount("100.00")
                .currency("USD")
                .deviceChannel("WEB")
                .isJSEnabled(true)
                .isTest(false)
                .timeout(300)
                .otpLength(6)
                .build();
    }

    @Test
    void testToCResTransactionAsNull() {
        // Act
        CRES cRes = cResMapper.toCRes(null, null);
        assertNull(cRes);
    }

    private static Image createImage(String imageUrl) {
        Image img = new Image();
        img.setHigh("imageUrl");
        img.setExtraHigh("imageUrl");
        return img;
    }

    private static ChallengeSelectInfo createChallengeSelectInfo() {
        return ChallengeSelectInfo.builder()
                .phone("1234")
                .sms("1234")
                .email("emkfkf")
                .yes("y")
                .build();
    }
}
