package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.enums.ACSRenderingType;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.WhitelistStatusSource;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class RReqMapperTest {

    private final RReqMapper rReqMapper = Mappers.getMapper(RReqMapper.class);

    @Test
    void testToRreqBrw() {
        // Given
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setEci("12");
        // When
        RREQ result = rReqMapper.toRreq(transaction);

        // Then
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getAcsTransID());
        assertEquals("12", result.getEci());
        assertEquals(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId(),
                result.getThreeDSServerTransID());
        assertEquals(transaction.getAuthValue(), result.getAuthenticationValue());
        assertEquals("02", result.getAuthenticationType());
        assertEquals(transaction.getTransactionStatusReason(), result.getTransStatusReason());
        assertEquals(transaction.getTransactionStatus().getStatus(), result.getTransStatus());
        assertEquals("01", result.getInteractionCounter());
        assertEquals("02", result.getAuthenticationMethod());
        assertEquals(transaction.getChallengeCancelInd(), result.getChallengeCancel());
        assertEquals(
                transaction.getTransactionReferenceDetail().getDsTransactionId(),
                result.getDsTransID());
        assertEquals(transaction.getMessageVersion(), result.getMessageVersion());
        assertEquals(MessageCategory.PA.getCategory(), result.getMessageCategory());
        assertEquals(MessageType.RReq.toString(), result.getMessageType());
    }

    @Test
    void testToRreqApp() {
        // Given
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setAuthenticationType(12);
        transaction.setInteractionCount(1);
        transaction.getTransactionSdkDetail().setWhitelistingDataEntry("Y");
        // When
        RREQ result = rReqMapper.toRreq(transaction);

        // Then
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getAcsTransID());
        assertEquals(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId(),
                result.getThreeDSServerTransID());
        assertEquals(transaction.getAuthValue(), result.getAuthenticationValue());
        assertEquals("12", result.getAuthenticationType());
        assertEquals(transaction.getTransactionStatusReason(), result.getTransStatusReason());
        assertEquals(transaction.getTransactionStatus().getStatus(), result.getTransStatus());
        assertEquals("01", result.getInteractionCounter());
        assertEquals("02", result.getAuthenticationMethod());
        assertEquals(transaction.getChallengeCancelInd(), result.getChallengeCancel());
        assertEquals(
                transaction.getTransactionReferenceDetail().getDsTransactionId(),
                result.getDsTransID());
        assertEquals(transaction.getMessageVersion(), result.getMessageVersion());
        assertEquals(MessageCategory.PA.getCategory(), result.getMessageCategory());
        assertEquals(MessageType.RReq.toString(), result.getMessageType());
        assertEquals(transaction.getTransactionSdkDetail().getSdkTransId(), result.getSdkTransID());
        assertEquals(
                new ACSRenderingType(
                        transaction.getTransactionSdkDetail().getAcsInterface(),
                        transaction.getTransactionSdkDetail().getAcsUiTemplate()),
                result.getAcsRenderingType());
        assertEquals("Y", result.getWhiteListStatus());
        assertEquals(WhitelistStatusSource.ACS.getValue(), result.getWhiteListStatusSource());
    }

    @Test
    void testToRreqNull() {
        assertNull(rReqMapper.toRreq(null));
    }

    @Test
    void testToRreqApp2() {
        // Given
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setAuthenticationType(2);
        transaction.setInteractionCount(11);
        transaction.getTransactionSdkDetail().setWhitelistingDataEntry("N");
        // When
        RREQ result = rReqMapper.toRreq(transaction);

        // Then
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getAcsTransID());
        assertEquals(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId(),
                result.getThreeDSServerTransID());
        assertEquals(transaction.getAuthValue(), result.getAuthenticationValue());
        assertEquals("02", result.getAuthenticationType());
        assertEquals(transaction.getTransactionStatusReason(), result.getTransStatusReason());
        assertEquals(transaction.getTransactionStatus().getStatus(), result.getTransStatus());
        assertEquals("11", result.getInteractionCounter());
        assertEquals("02", result.getAuthenticationMethod());
        assertEquals(transaction.getChallengeCancelInd(), result.getChallengeCancel());
        assertEquals(
                transaction.getTransactionReferenceDetail().getDsTransactionId(),
                result.getDsTransID());
        assertEquals(transaction.getMessageVersion(), result.getMessageVersion());
        assertEquals(MessageCategory.PA.getCategory(), result.getMessageCategory());
        assertEquals(MessageType.RReq.toString(), result.getMessageType());
        assertEquals(transaction.getTransactionSdkDetail().getSdkTransId(), result.getSdkTransID());
        assertEquals(
                new ACSRenderingType(
                        transaction.getTransactionSdkDetail().getAcsInterface(),
                        transaction.getTransactionSdkDetail().getAcsUiTemplate()),
                result.getAcsRenderingType());
        assertEquals("R", result.getWhiteListStatus());
        assertEquals(WhitelistStatusSource.ACS.getValue(), result.getWhiteListStatusSource());
    }
}
