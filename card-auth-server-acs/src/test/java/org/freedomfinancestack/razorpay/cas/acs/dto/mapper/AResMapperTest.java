package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AResMapperParams;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AResMapperTest {
    @InjectMocks private AResMapperImpl aResMapperImpl;
    @Mock private HelperMapper helperMapper;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
    }

    @Test
    void testToAres() {
        // Arrange
        when(helperMapper.getAppConfiguration())
                .thenReturn(TransactionTestData.createSampleAcsConfig());
        AREQ areq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleBrwTransaction();
        transaction.setTransactionStatusReason(TransactionStatusReason.SUSPECTED_FRAUD.getCode());
        AResMapperParams aResMapperParams = new AResMapperParams("acsSignedContent");

        // Act
        ARES ares = aResMapperImpl.toAres(areq, transaction, aResMapperParams);

        // Assert
        assertNotNull(ares);
        // Fields from AREQ
        assertEquals(areq.getThreeDSServerTransID(), ares.getThreeDSServerTransID());
        assertEquals(areq.getDsReferenceNumber(), ares.getDsReferenceNumber());
        assertEquals(areq.getDsTransID(), ares.getDsTransID());
        assertEquals(areq.getSdkTransID(), ares.getSdkTransID());
        assertEquals(areq.getMessageVersion(), ares.getMessageVersion());
        assertEquals(areq.getMessageExtension(), ares.getMessageExtension());

        // Fields from Transaction
        assertEquals("N", ares.getAcsDecConInd());
        assertEquals(transaction.getId(), ares.getAcsTransID());
        assertEquals(transaction.getEci(), ares.getEci());
        assertEquals(transaction.getAuthValue(), ares.getAuthenticationValue());
        assertEquals(transaction.getTransactionStatus().getStatus(), ares.getTransStatus());
        assertEquals(transaction.getTransactionStatusReason(), ares.getTransStatusReason());

        // Other fields
        assertEquals("N", ares.getAcsDecConInd());
        assertEquals("VISA", ares.getAcsOperatorID());
        assertEquals("referenceNumber", ares.getAcsReferenceNumber());
        assertEquals("www.test.com/v2/transaction/challenge/browser", ares.getAcsURL());
        assertEquals("02", ares.getAuthenticationType());
        assertNull(ares.getBroadInfo());
        assertEquals("ARes", ares.getMessageType());
        assertNull(ares.getWhiteListStatus());
        assertNull(ares.getWhiteListStatusSource());
        assertNull(ares.getAcsRenderingType());
        assertNull(ares.getCardholderInfo());
    }

    @Test
    void testToAresWithAPP_Decoupled() {
        // Arrange
        when(helperMapper.getAppConfiguration())
                .thenReturn(TransactionTestData.createSampleAcsConfig());
        AREQ areq = AREQTestData.createSampleAREQ();
        areq.setThreeRIInd("10");
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED);
        AResMapperParams aResMapperParams = new AResMapperParams("acsSignedContent");

        // Act
        ARES ares = aResMapperImpl.toAres(areq, transaction, aResMapperParams);

        // Assert
        assertNotNull(ares);
        // Fields from AREQ
        assertEquals(areq.getThreeDSServerTransID(), ares.getThreeDSServerTransID());
        assertEquals(areq.getDsReferenceNumber(), ares.getDsReferenceNumber());
        assertEquals(areq.getDsTransID(), ares.getDsTransID());
        assertEquals(areq.getSdkTransID(), ares.getSdkTransID());
        assertEquals(areq.getMessageVersion(), ares.getMessageVersion());
        assertEquals(areq.getMessageExtension(), ares.getMessageExtension());

        // Fields from Transaction
        assertEquals(transaction.getId(), ares.getAcsTransID());
        assertEquals(transaction.getEci(), ares.getEci());
        assertEquals(transaction.getAuthValue(), ares.getAuthenticationValue());
        assertEquals(transaction.getTransactionStatus().getStatus(), ares.getTransStatus());
        assertEquals(transaction.getTransactionStatusReason(), ares.getTransStatusReason());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsInterface(),
                ares.getAcsRenderingType().getAcsInterface());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsUiTemplate(),
                ares.getAcsRenderingType().getAcsUiTemplate());

        // Fields from AResMapperParams
        assertEquals(aResMapperParams.getAcsSignedContent(), ares.getAcsSignedContent());

        // Other fields
        assertEquals("Y", ares.getAcsDecConInd());
        assertEquals("VISA", ares.getAcsOperatorID());
        assertEquals("referenceNumber", ares.getAcsReferenceNumber());
        assertNull(ares.getAcsURL());
        assertEquals("02", ares.getAuthenticationType());
        assertEquals("N", ares.getWhiteListStatus());
        assertEquals("03", ares.getWhiteListStatusSource());
        assertEquals("ARes", ares.getMessageType());
        assertEquals(
                "Additional authentication is needed for this transaction, please  contact (Issuer"
                        + " Name) at xxx-xxx-xxxx.",
                ares.getCardholderInfo());
    }

    @Test
    void testToAresWithfailedMcVersion210() {
        // Arrange
        when(helperMapper.getAppConfiguration())
                .thenReturn(TransactionTestData.createSampleAcsConfig());
        AREQ areq = AREQTestData.createSampleAREQ();
        areq.setMessageVersion("2.1.0");
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.getTransactionCardDetail().setNetworkCode(Network.MASTERCARD.getValue());
        transaction.setAuthenticationType(5);
        transaction.setMessageVersion("2.1.0");

        // Act
        ARES ares = aResMapperImpl.toAres(areq, transaction, null);

        // Assert
        assertNotNull(ares);
        // Fields from AREQ
        assertEquals(areq.getThreeDSServerTransID(), ares.getThreeDSServerTransID());
        assertEquals(areq.getDsReferenceNumber(), ares.getDsReferenceNumber());
        assertEquals(areq.getDsTransID(), ares.getDsTransID());
        assertEquals(areq.getSdkTransID(), ares.getSdkTransID());
        assertEquals(areq.getMessageVersion(), ares.getMessageVersion());
        assertEquals(areq.getMessageExtension(), ares.getMessageExtension());

        // Fields from Transaction
        assertEquals(transaction.getId(), ares.getAcsTransID());
        assertEquals(transaction.getEci(), ares.getEci());
        assertEquals(transaction.getAuthValue(), ares.getAuthenticationValue());
        assertEquals(transaction.getTransactionStatus().getStatus(), ares.getTransStatus());
        assertEquals(transaction.getTransactionStatusReason(), ares.getTransStatusReason());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsInterface(),
                ares.getAcsRenderingType().getAcsInterface());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsUiTemplate(),
                ares.getAcsRenderingType().getAcsUiTemplate());

        // Other fields
        assertEquals("MASTERCARD", ares.getAcsOperatorID());
        assertEquals("referenceNumber", ares.getAcsReferenceNumber());
        assertNull(ares.getAcsURL());
        assertEquals("05", ares.getAuthenticationType());
        assertEquals("ARes", ares.getMessageType());
        assertNull(ares.getCardholderInfo());
    }

    @Test
    void testToAresWithAmex() {
        // Arrange
        when(helperMapper.getAppConfiguration())
                .thenReturn(TransactionTestData.createSampleAcsConfig());
        AREQ areq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.getTransactionCardDetail().setNetworkCode(Network.AMEX.getValue());
        transaction.setAuthenticationType(15);

        // Act
        ARES ares = aResMapperImpl.toAres(areq, transaction, null);

        // Assert
        assertNotNull(ares);
        // Fields from AREQ
        assertEquals(areq.getThreeDSServerTransID(), ares.getThreeDSServerTransID());
        assertEquals(areq.getDsReferenceNumber(), ares.getDsReferenceNumber());
        assertEquals(areq.getDsTransID(), ares.getDsTransID());
        assertEquals(areq.getSdkTransID(), ares.getSdkTransID());
        assertEquals(areq.getMessageVersion(), ares.getMessageVersion());
        assertEquals(areq.getMessageExtension(), ares.getMessageExtension());

        // Fields from Transaction
        assertEquals(transaction.getId(), ares.getAcsTransID());
        assertEquals(transaction.getEci(), ares.getEci());
        assertEquals(transaction.getAuthValue(), ares.getAuthenticationValue());
        assertEquals(transaction.getTransactionStatus().getStatus(), ares.getTransStatus());
        assertEquals(
                TransactionStatusReason.MEDIUM_CONFIDENCE.getCode(), ares.getTransStatusReason());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsInterface(),
                ares.getAcsRenderingType().getAcsInterface());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsUiTemplate(),
                ares.getAcsRenderingType().getAcsUiTemplate());

        // Other fields
        assertEquals("AMEX", ares.getAcsOperatorID());
        assertEquals("referenceNumber", ares.getAcsReferenceNumber());
        assertNull(ares.getAcsURL());
        assertEquals("15", ares.getAuthenticationType());
        assertEquals("ARes", ares.getMessageType());
        assertNull(ares.getCardholderInfo());
    }

    @Test
    void testToAresWithOutNetworkCode() {
        // Arrange
        when(helperMapper.getAppConfiguration())
                .thenReturn(TransactionTestData.createSampleAcsConfig());
        AREQ areq = AREQTestData.createSampleAREQ();
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.getTransactionCardDetail().setNetworkCode(null);

        // Act
        ARES ares = aResMapperImpl.toAres(areq, transaction, null);

        // Assert
        assertNotNull(ares);
        // Fields from AREQ
        assertEquals(areq.getThreeDSServerTransID(), ares.getThreeDSServerTransID());
        assertEquals(areq.getDsReferenceNumber(), ares.getDsReferenceNumber());
        assertEquals(areq.getDsTransID(), ares.getDsTransID());
        assertEquals(areq.getSdkTransID(), ares.getSdkTransID());
        assertEquals(areq.getMessageVersion(), ares.getMessageVersion());
        assertEquals(areq.getMessageExtension(), ares.getMessageExtension());

        // Fields from Transaction
        assertEquals(transaction.getId(), ares.getAcsTransID());
        assertEquals(transaction.getEci(), ares.getEci());
        assertEquals(transaction.getAuthValue(), ares.getAuthenticationValue());
        assertEquals(transaction.getTransactionStatus().getStatus(), ares.getTransStatus());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsInterface(),
                ares.getAcsRenderingType().getAcsInterface());
        assertEquals(
                transaction.getTransactionSdkDetail().getAcsUiTemplate(),
                ares.getAcsRenderingType().getAcsUiTemplate());

        // Other fields
        assertEquals("DEFAULT", ares.getAcsOperatorID());
        assertEquals("referenceNumber", ares.getAcsReferenceNumber());
        assertNull(ares.getAcsURL());
        assertEquals("02", ares.getAuthenticationType());
        assertEquals("ARes", ares.getMessageType());
        assertNull(ares.getCardholderInfo());
    }
}
